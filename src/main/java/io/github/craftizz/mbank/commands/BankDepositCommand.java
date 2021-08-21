package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.hooks.VaultHook;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command("mb")
@Alias("bank")
public class BankDepositCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankDepositCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("deposit")
    public void onBankDepositCommand(final @NotNull Player player,
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
        if (amount >= 0) {
            MessageUtil.sendMessage(player,
                    Language.NEGATIVE_NOT_ALLOWED,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        final UserBankData userBankData = bankDataOptional.get();
        final double bankBalance = userBankData.getBalance();
        final double playerBalance = VaultHook.getEconomy().getBalance(player);

        // Check if player has enough money
        if (playerBalance < amount) {
            MessageUtil.sendMessage(player,
                    Language.NOT_ENOUGH_MONEY,
                    MessageType.DENY,
                    "amount", String.valueOf(amount));
            return;
        }

        // Check Restriction
        if (bank.getRestrictions().getMaximumBalance() < (bankBalance + amount)) {
            MessageUtil.sendMessage(player,
                    Language.MAXIMUM_BALANCE_REACHED,
                    MessageType.DENY,
                    "bank", bankName,
                    "amount", String.valueOf(amount));
            return;
        }

        // Deposit Amount
        bankManager.deposit(bank, player, amount);
        MessageUtil.sendMessage(player,
                Language.BANK_DEPOSITED,
                MessageType.INFORMATION,
                "bank", bankName,
                "amount", String.valueOf(amount),
                "fee", String.valueOf(bank.getFees().calculateDepositFee(amount)));
    }

}
