package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.hooks.VaultHook;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("mb")
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
                                      final @NotNull String bankName,
                                      final @NotNull Double amount) {

        final Double playerBalance = VaultHook.getEconomy().getBalance(player);
        // Check Minimum Balance

        final Bank bank = bankManager.getBank(bankName);
        bankManager.withdraw(bank, player, amount);
    }
}