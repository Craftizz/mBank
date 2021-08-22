package io.github.craftizz.mbank.tasks;

import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.PlayerManager;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Interest;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.hooks.CMIHook;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.tasks.tasktypes.TimedTask;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InterestTask extends TimedTask {

    private final String bankId;

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public InterestTask(final @NotNull MBank plugin,
                        final @NotNull String bankId) {
        super(1);
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
        this.plugin = plugin;
        this.bankId = bankId;
    }

    /**
     * This checks if the bank should have the interest happen,
     * if true, it will execute the interest payout which respects
     * all the values in {@link Interest}
     */
    @Override
    public void compute() {

        final Bank bank = bankManager.getBank(bankId);
        final Interest interest = bank.getInterest();

        if (!interest.shouldGiveInterest()) return;

        interest.calculateNextPayout();

        final PlayerManager playerManager = CMIHook.getCmi().getPlayerManager();
        final double bankLimit = bank.getRestrictions().getMaximumBalance();
        
        plugin.getLogger().warning(bankId + " will begin giving interest");
        
        for (final Player player : Bukkit.getOnlinePlayers()) {

            final CMIUser cmiUser = playerManager.getUser(player);

            if (cmiUser.isAfk()) {
                continue;
            }

            userManager.getUser(player)
                    .getUserBankData(bank.getId())
                    .ifPresent(bankData -> {

                        double interestEarning = interest.calculateInterest(bankData.getBalance());

                        if (interestEarning != 0d) {

                            final double newBalance = interestEarning + bankData.getBalance();

                            if (newBalance > bankLimit) {
                                interestEarning = newBalance - Math.max(newBalance, bankLimit);
                            }

                            bankData.deposit(interestEarning);

                            if (interestEarning == 0d) {
                                MessageUtil.sendMessage(player,
                                        Language.INTEREST_MAX_BALANCE,
                                        MessageType.INFORMATION,
                                        "bank", bank.getId());
                            }

                            else {
                                MessageUtil.sendMessage(player,
                                        Language.BANK_INTEREST_EARN,
                                        MessageType.INFORMATION,
                                        "bank", bank.getId(),
                                        "amount", NumberUtils.formatCurrency(interestEarning));
                            }
                        }
                    });
        }
    }
}
