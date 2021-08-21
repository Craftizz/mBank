package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Restrictions;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Command("mb")
@Alias("bank")
public class BankWithdrawCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankWithdrawCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("withdraw")
    public void onBankWithdrawCommand(final @NotNull Player player,
                                      final @NotNull @Completion("#banks") String bankName,
                                      final @NotNull @Completion("#amount") Double amount) {

        final Bank bank = bankManager.getBank(bankName);
        final User user = userManager.getUser(player);

        // Check if bank exists
        if (bank == null) {
            MessageUtil.sendMessage(player,
                    Language.NONEXISTENT_BANK,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        // Check if player is a bankUser of the bank
        final Optional<UserBankData> bankDataOptional = user.getUserBankData(bankName);

        if (bankDataOptional.isEmpty()) {
            MessageUtil.sendMessage(player,
                    Language.NOT_IN_BANK,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        // Check if amount is negative
        if (amount <= 0) {
            MessageUtil.sendMessage(player,
                    Language.NEGATIVE_NOT_ALLOWED,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        final UserBankData userBankData = bankDataOptional.get();
        final Restrictions restrictions = bank.getRestrictions();
        final double bankBalance = userBankData.getBalance();

        // Check last withdrawn
        final LocalDateTime currentTime = LocalDateTime.now();
        final LocalDateTime allowedTime = userBankData
                .getLastWithdraw()
                .plusSeconds(restrictions.getWithdrawInterval());

        if (allowedTime.isBefore(currentTime)) {
            MessageUtil.sendMessage(player,
                    Language.WITHDRAW_DENY_TIME,
                    MessageType.DENY,
                    "bank", bankName,
                    "time", NumberUtils.convertSeconds(ChronoUnit.SECONDS.between(currentTime, allowedTime)));
            return;
        }

        // Check if player has enough money in the bank
        if (bankBalance < amount) {
            MessageUtil.sendMessage(player,
                    Language.NOT_ENOUGH_MONEY_IN_BANK,
                    MessageType.DENY,
                    "bank", bankName,
                    "amount", NumberUtils.formatCurrency(amount));
            return;
        }

        bankManager.withdraw(bank, player, amount);

        final double fee = bank.getFees().calculateWithdrawFee(amount);
        MessageUtil.sendMessage(player,
                Language.BANK_WITHDRAWN,
                MessageType.INFORMATION,
                "bank", bankName,
                "amount", NumberUtils.formatCurrency(amount - fee),
                "fee", NumberUtils.formatCurrency(fee));

    }
}
