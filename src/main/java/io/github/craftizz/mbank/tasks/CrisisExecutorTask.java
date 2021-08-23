package io.github.craftizz.mbank.tasks;

import com.google.common.collect.Queues;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Crisis;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.tasks.tasktypes.BooleanTask;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.UUID;

public class CrisisExecutorTask extends BooleanTask {

    private final ArrayDeque<UUID> offlinePlayers;

    private final BankManager bankManager;
    private final UserManager userManager;
    private final String bankId;

    public CrisisExecutorTask(final @NotNull MBank plugin,
                              final @NotNull String bankId) {
        this.offlinePlayers = Queues.newArrayDeque();
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
        this.bankId = bankId;

        for (final OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            offlinePlayers.add(offlinePlayer.getUniqueId());
        }
    }

    @Override
    public void compute() {

        // Get stop time
        final long stopTime = System.currentTimeMillis() + 5;

        // Get necessary data
        final Bank bank = bankManager.getBank(bankId);
        final Crisis crisis = bank.getCrisis();

        // Loop
        while (!offlinePlayers.isEmpty() && System.currentTimeMillis() <= stopTime) {

            final UUID uniqueId = offlinePlayers.poll();

            if (uniqueId == null) {
                continue;
            }

            // Get User
            userManager.getUser(uniqueId)
                    .getUserBankData(bankId)
                    .ifPresent(bankData -> {

                        // Calculate Lost
                        double totalLost = crisis.calculateLost(bankData.getBalance());

                        // Set data
                        bankData.setLostInLastCrisis(totalLost);
                        bankData.addToTotalLostInCrisis(totalLost);
                        bankData.withdraw(totalLost);

                        // Send message if online
                        final Player player = Bukkit.getPlayer(uniqueId);

                        if (player != null) {

                            if (totalLost == 0d) {
                                MessageUtil.sendMessage(player,
                                        Language.BANK_CRISIS_NOT_AFFECTED,
                                        MessageType.INFORMATION,
                                        "bank", bank.getId());
                            }

                            else {
                                MessageUtil.sendMessage(player,
                                        Language.BANK_CRISIS_LOST,
                                        MessageType.INFORMATION,
                                        "bank", bank.getId(),
                                        "amount", NumberUtils.formatCurrency(totalLost));
                            }
                        }

            });
        }

        // Cancel task if done
        if (offlinePlayers.isEmpty()) {
            setShouldReschedule(false);
        }

    }
}
