package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("mb")
@Alias("bank")
public class BankBalanceCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankBalanceCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("balance")
    public void onBankBalanceCommand(final @NotNull Player player,
                                     final @Optional @Completion("#banks") String bankName) {

        final User user = userManager.getUser(player);

        if (bankName != null) {

            final java.util.Optional<UserBankData> optionalUserBankData = user.getUserBankData(bankName);

            // Check if player is a bankUser of the bank
            if (optionalUserBankData.isEmpty()) {
                MessageUtil.sendMessage(player,
                        Language.NOT_IN_BANK,
                        MessageType.DENY,
                        "bank", bankName);
                return;
            }

            // Send bank balance
            final UserBankData userBankData = optionalUserBankData.get();
            MessageUtil.sendMessage(player,
                    Language.BANK_BALANCE_SPECIFIC,
                    MessageType.INFORMATION,
                    "bank", bankName,
                    "amount", NumberUtils.formatCurrency(userBankData.getBalance()));

            return;
        }

        // Check if has a bank data
        if (user.getBankData().isEmpty()) {
            MessageUtil.sendMessage(player,
                    Language.DO_NOT_HAVE_BANK,
                    MessageType.DENY);
            return;
        }

        // Get the total balance
        final Double totalBalance = user.getBankData()
                .stream()
                .mapToDouble(UserBankData::getBalance)
                .sum();

        // Send message to player
        MessageUtil.sendMessage(player,
                Language.BANK_BALANCE,
                MessageType.INFORMATION,
                "amount", NumberUtils.formatCurrency(totalBalance));
    }
}
