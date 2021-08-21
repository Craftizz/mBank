package io.github.craftizz.mbank.tasks;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Crisis;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.tasks.tasktypes.TimedTask;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class CrisisTask extends TimedTask {

    private final String bankId;

    private final BankManager bankManager;
    private final UserManager userManager;

    public CrisisTask(final @NotNull MBank plugin,
                      final @NotNull String bankId) {
        super(1);
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
        this.bankId = bankId;
    }

    /**
     * This checks if the bank should have the crisis happen,
     * if true, it will execute the crisis which respects
     * all the values in {@link Crisis}
     *
     */
    @Override
    public void compute() {

        final Bank bank = bankManager.getBank(bankId);
        final Crisis crisis = bank.getCrisis();

        // Remove
        Bukkit.getLogger().info("Crisis in " + crisis.getNextCrisis());

        if (!crisis.shouldHappen()) return;

        // Remove
        Bukkit.getLogger().severe("Crisis is Happening!");

        for (final OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            userManager.getUser(offlinePlayer)
                    .getUserBankData(bank.getId())
                    .ifPresent(bankData -> {
                        double totalLost = crisis.calculateLost(bankData.getBalance());
                        bankData.setLostInLastCrisis(totalLost);
                        bankData.withdraw(totalLost);
                    });
        }
    }

}
