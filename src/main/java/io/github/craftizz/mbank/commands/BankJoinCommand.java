package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Restrictions;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.hooks.VaultHook;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("mb")
public class BankJoinCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankJoinCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("join")
    public void onBankJoinCommand(final @NotNull Player player,
                                  final @NotNull @Completion("#banks") String bankName) {

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

        // Check if player already joined the bank
        if (user.getUserBankData(bankName).isPresent()) {
            MessageUtil.sendMessage(player,
                    Language.ALREADY_JOINED_BANK,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        // Start Checking Restrictions
        final Restrictions restrictions = bank.getRestrictions();

        // Check if player has permission to join the bank
        for (String permission : restrictions.getPermissionRequirements()) {
            if (!VaultHook.getPermission().playerHas(player, permission)) {
                MessageUtil.sendMessage(player,
                        Language.NO_PERMISSION_TO_JOIN,
                        MessageType.DENY,
                        "bank", bankName);
                return;
            }
        }

        // Check if player already have minimum balance
        final Double playerBalance = VaultHook.getEconomy().getBalance(player);
        final Double minimumBalance = restrictions.getMinimumBalance();

        if (minimumBalance >= playerBalance) {
            MessageUtil.sendMessage(player,
                    Language.NOT_ENOUGH_MINIMUM_BALANCE,
                    MessageType.DENY,
                    "bank", bankName,
                    "amount", String.valueOf(minimumBalance));
            return;
        }

        // Create Account
        bankManager.createAccount(bank, player);
        MessageUtil.sendMessage(player,
                Language.BANK_JOINED,
                MessageType.INFORMATION,
                "bank", bankName);
    }
}
