package io.github.craftizz.mbank.managers;

import de.leonhard.storage.Yaml;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.gui.BankStatisticsGUI;
import io.github.craftizz.mbank.tasks.GUITask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GUIManager {

    private final MBank plugin;

    public GUIManager(MBank plugin) {
        this.plugin = plugin;
    }

    public void openBankStatisticsGUI(final @NotNull Player player,
                                      final @NotNull UserBankData userBankData,
                                      final @NotNull Bank bank,
                                      final @NotNull Yaml guiYaml) {

        final BankStatisticsGUI bankStatisticsGUI = new BankStatisticsGUI(userBankData, bank, guiYaml);
        final GUITask guiTask = new GUITask(bankStatisticsGUI);

        plugin.getTaskManager().addTask(guiTask);
        bankStatisticsGUI.open(player);
    }
}
