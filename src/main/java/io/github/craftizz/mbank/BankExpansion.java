package io.github.craftizz.mbank;

import io.github.craftizz.mbank.bank.*;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.NumberUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankExpansion extends PlaceholderExpansion {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankExpansion(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mbank";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(",", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of(
                "%mbank_<bank>_displayname%",
                "%mbank_<bank>_interest_timeleft%",
                "%mbank_<bank>_interest_timeleft_formatted%",
                "%mbank_<bank>_interest_interval%",
                "%mbank_<bank>_interest_interval_formatted%",
                "%mbank_<bank>_interest_value%",
                "%mbank_<bank>_fees_deposit%",
                "%mbank_<bank>_fees_withdraw%",
                "%mbank_<bank>_restriction_minimum%",
                "%mbank_<bank>_restriction_minimum_formatted%",
                "%mbank_<bank>_restriction_maximum%",
                "%mbank_<bank>_restriction_maximum_formatted%",
                "%mbank_<bank>_restriction_withdrawinterval",
                "%mbank_<bank>_restriction_withdrawinterval_formatted",
                "%mbank_<bank>_restriction_permissions",
                "%mbank_<bank>_crisis_chancetohappen%",
                "%mbank_<bank>_crisis_chancetolose%",
                "%mbank_<bank>_crisis_minimumlost%",
                "%mbank_<bank>_crisis_maximumlost%",
                "%mbank_<bank>_crisis_interval%",
                "%mbank_<bank>_crisis_interval_formatted%",
                "%mbank_<bank>_crisis_timeleft%",
                "%mbank_<bank>_crisis_timeleft_formatted%",
                "%mbank_player_<bank>_balance%",
                "%mbank_player_<bank>_balance_formatted%",
                "%mbank_player_<bank>_withdrawin%",
                "%mbank_player_<bank>_withdrawin_formatted%",
                "%mbank_player_<bank>_lastcrisis%",
                "%mbank_player_<bank>_lastcrisis_formatted%",
                "%mbank_player_<bank>_crisis_minimum%",
                "%mbank_player_<bank>_crisis_minimum_formatted%",
                "%mbank_player_<bank>_crisis_maximum%",
                "%mbank_player_<bank>_crisis_maximum_formatted%",
                "%mbank_player_<bank>_nextinterest%",
                "%mbank_player_<bank>_nextinterest_formatted",
                "%mbank_player_banks_names",
                "%mbank_player_banks_slots",
                "%mbank_player_balance",
                "%mbank_player_balance_formatted",
                "%mbank_player_interest%",
                "%mbank_player_interest_formatted%"
        );
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String placeholder) {
        return retrieveValue(offlinePlayer, placeholder.toLowerCase().split("_"));
    }

    private String retrieveValue(final @NotNull OfflinePlayer offlinePlayer,
                                 final @NotNull String... arguments) {

        switch (arguments[0]) {
            case "player": return getPlayerPlaceholder(offlinePlayer, arguments);
            default: return getDefaultPlaceholders(offlinePlayer, arguments);
        }

    }

    private String getPlayerPlaceholder(final @NotNull OfflinePlayer offlinePlayer,
                                        final @NotNull String... arguments) {

        final User user = userManager.getUser(offlinePlayer);

        switch (arguments[1]) {

            case "banks":
                if (arguments[2].equals("names")) {
                    return user.getBankData()
                            .stream()
                            .map(UserBankData::getBankId)
                            .collect(Collectors.joining());
                }
                else if (arguments[2].equals("slots")) {
                    return String.valueOf(user.getBankData().size());
                }

            case "balance":
                if (arguments.length == 3) {
                    return String.valueOf(user.getBankData()
                            .stream()
                            .mapToDouble(UserBankData::getBalance)
                            .sum());
                }
                else if (arguments[2].equals("formatted")) {
                    return NumberUtils.formatCurrency(user.getBankData()
                            .stream()
                            .mapToDouble(UserBankData::getBalance)
                            .sum());
                }

            case "interest":
                if (arguments.length == 3) {
                    return String.valueOf(user.getBankData().stream()
                            .mapToDouble(bankData -> bankData.getBalance() * bankManager
                                    .getBank(bankData.getBankId())
                                    .getInterest()
                                    .getInterest())
                            .sum());
                }
                else if (arguments[2].equals("formatted")) {
                    return NumberUtils.formatCurrency(user.getBankData().stream()
                            .mapToDouble(bankData -> bankData.getBalance() * bankManager
                                    .getBank(bankData.getBankId())
                                    .getInterest()
                                    .getInterest())
                            .sum());
                }

            default:

                final Optional<UserBankData> userBankData = user.getUserBankData(arguments[1]);

                if (userBankData.isEmpty()) {
                    return "";
                }

                final UserBankData bankData = userBankData.get();
                final Bank bank = bankManager.getBank(bankData.getBankId());

                if (bank == null) {
                    return null;
                }

                switch (arguments[2]) {

                    case "balance":
                        if (arguments.length == 3) {
                            return String.valueOf(bankData.getBalance());
                        }
                        else if (arguments[2].equals("formatted")) {
                            return NumberUtils.formatCurrency(bankData.getBalance());
                        }

                    case "withdrawin":
                        if (arguments.length == 3) {
                            return String.valueOf(LocalDateTime.now().until(bankData.getLastWithdraw().plusSeconds(bank.getRestrictions().getWithdrawInterval()), ChronoUnit.SECONDS));
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.convertSeconds(LocalDateTime.now().until(bankData.getLastWithdraw().plusSeconds(bank.getRestrictions().getWithdrawInterval()), ChronoUnit.SECONDS));
                        }

                    case "lastcrisis":
                        if (arguments.length == 3) {
                            return String.valueOf(bankData.getLostInLastCrisis());
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.formatCurrency(bankData.getLostInLastCrisis());
                        }

                    case "nextcrisis":


                        if (arguments[3].equals("maximum")) {

                            if (arguments.length == 4) {
                                return String.valueOf(bankData.getBalance() * bank.getCrisis().getMaximumLostInPercentage());
                            }
                            else if (arguments[4].equals("formatted")) {
                                return NumberUtils.formatCurrency(bankData.getBalance() * bank.getCrisis().getMaximumLostInPercentage());
                            }

                        }

                        else if (arguments[3].equals("minimum")) {

                            if (arguments.length == 4) {
                                return String.valueOf(bankData.getBalance() * bank.getCrisis().getMinimumLostInPercentage());
                            }
                            else if (arguments[4].equals("formatted")) {
                                return NumberUtils.formatCurrency(bankData.getBalance() * bank.getCrisis().getMinimumLostInPercentage());
                            }
                        }

                    case "nextinterest":

                        if (arguments.length == 3) {
                            return String.valueOf(bank.getInterest().calculateInterest(bankData.getBalance()));
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.formatCurrency(bank.getInterest().calculateInterest(bankData.getBalance()));
                        }

                }
        }

        return null;
    }

    public String getDefaultPlaceholders(final @NotNull OfflinePlayer offlinePlayer,
                                         final @NotNull String... arguments) {

        final Bank bank = bankManager.getBank(arguments[1]);

        if (bank == null) {
            return null;
        }

        switch(arguments[1]) {

            case "displayname":
                return bank.getDisplayName();

            case "interest":

                final Interest interest = bank.getInterest();
                switch (arguments[2]) {

                    case "timeleft":
                        if (arguments.length == 3) {
                            return String.valueOf(interest.getNextPayout());
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.convertSeconds(interest.getNextPayout());
                        }

                    case "interval":
                        if (arguments.length == 3) {
                            return String.valueOf(interest.getPayoutInterval());
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.convertSeconds(interest.getPayoutInterval());
                        }

                    case "value":
                        return String.valueOf(interest.getInterest());

                }

            case "fees":

                final Fees fees = bank.getFees();
                if (arguments[2].equals("deposit")) {
                    return String.valueOf(fees.getDepositFee());
                }
                if (arguments[2].equals("withdraw")) {
                    return String.valueOf(fees.getWithdrawFee());
                }

            case "restriction":

                final Restrictions restrictions = bank.getRestrictions();
                switch (arguments[2]) {

                    case "minimum":
                        if (arguments.length == 3) {
                            return String.valueOf(restrictions.getMinimumBalance());
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.formatCurrency(restrictions.getMinimumBalance());
                        }

                    case "maximum":
                        if (arguments.length == 3) {
                            return String.valueOf(restrictions.getMaximumBalance());
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.formatCurrency(restrictions.getMaximumBalance());
                        }

                    case "withdrawinterval":
                        if (arguments.length == 3) {
                            return String.valueOf(restrictions.getWithdrawInterval());
                        }
                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.convertSeconds(restrictions.getWithdrawInterval());
                        }

                    case "permissions":
                        return String.join("\n", restrictions.getPermissionRequirements());
                }

            case "crisis":

                final Crisis crisis = bank.getCrisis();

                switch (arguments[2]) {

                    case "chancetohappen":
                        return String.valueOf(crisis.getChanceToHappen());

                    case "chancetolose":
                        return String.valueOf(crisis.getChanceToLose());

                    case "minimumlost":
                        return String.valueOf(crisis.getMinimumLostInPercentage());

                    case "maximumlost":
                        return String.valueOf(crisis.getMaximumLostInPercentage());

                    case "interval":

                        if (arguments[3].equals("minimum")) {
                            if (arguments.length == 4) {
                                return String.valueOf(crisis.getMinimumInterval());
                            } else if (arguments[4].equals("formatted")) {
                                return NumberUtils.convertSeconds(crisis.getMinimumInterval());
                            }
                        } else if (arguments[3].equals("maximum")) {
                            if (arguments.length == 4) {
                                return String.valueOf(crisis.getMaximumInterval());
                            } else if (arguments[4].equals("formatted")) {
                                return NumberUtils.convertSeconds(crisis.getMaximumInterval());
                            }
                        }

                    case "timeleft":

                        if (arguments.length == 3) {
                            return String.valueOf(crisis.getNextCrisis());
                        }

                        else if (arguments[3].equals("formatted")) {
                            return NumberUtils.convertSeconds(crisis.getNextCrisis());
                        }
                }
        }

        return null;
    }




}
