package io.github.craftizz.mbank.tasks;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Interest;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.tasks.tasktypes.TimedTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InterestTask extends TimedTask {

    private final String bankId;

    private final BankManager bankManager;
    private final UserManager userManager;

    public InterestTask(final @NotNull MBank plugin,
                        final @NotNull String bankId) {
        super(1);
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
        this.bankId = bankId;
    }

    @Override
    public void compute() {

        final Bank bank = bankManager.getBank(bankId);
        final Interest interest = bank.getInterest();

        if (!interest.shouldGiveInterest()) return;
        interest.calculateNextPayout();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            userManager.getUser(player)
                    .getUserBankData(bank.getId())
                    .ifPresent(bankData -> bankData.deposit(interest.calculateInterest(bankData.getBalance())));
        }
    }
}
