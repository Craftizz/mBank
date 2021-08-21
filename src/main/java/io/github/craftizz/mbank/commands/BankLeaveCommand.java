package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
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
public class BankLeaveCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankLeaveCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("leave")
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

        // Check if player is a bankUser of the bank
        if (user.getUserBankData(bankName).isEmpty()) {
            MessageUtil.sendMessage(player,
                    Language.NOT_IN_BANK,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        // Delete Account
        bankManager.deleteAccount(bank, player);
        MessageUtil.sendMessage(player,
                Language.BANK_JOINED,
                MessageType.INFORMATION,
                "bank", bankName);
    }
}
