package io.github.craftizz.mbank.tasks;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Crisis;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.tasks.tasktypes.TimedTask;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
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

        if (!crisis.shouldHappen()) return;

        for (final OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            userManager.getUser(offlinePlayer)
                    .getUserBankData(bank.getId())
                    .ifPresent(bankData -> {

                        double totalLost = crisis.calculateLost(bankData.getBalance());
                        bankData.setLostInLastCrisis(totalLost);
                        bankData.withdraw(totalLost);

                        if (offlinePlayer.isOnline()) {

                            if (totalLost == 0d) {
                                MessageUtil.sendMessage(offlinePlayer.getPlayer(),
                                        Language.BANK_CRISIS_NOT_AFFECTED,
                                        MessageType.INFORMATION,
                                        "bank", bank.getId());
                            }

                            else {
                                MessageUtil.sendMessage(offlinePlayer.getPlayer(),
                                        Language.BANK_CRISIS_NOT_AFFECTED,
                                        MessageType.INFORMATION,
                                        "bank", bank.getId(),
                                        "amount", NumberUtils.formatCurrency(totalLost));
                            }
                        }
                    });
        }
    }

}
