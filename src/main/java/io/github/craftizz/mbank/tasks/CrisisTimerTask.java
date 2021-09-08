package io.github.craftizz.mbank.tasks;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Crisis;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.TaskManager;
import io.github.craftizz.mbank.tasks.tasktypes.TimedTask;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class CrisisTimerTask extends TimedTask {

    private final String bankId;

    private final MBank plugin;
    private final BankManager bankManager;
    private final TaskManager taskManager;

    public CrisisTimerTask(final @NotNull MBank plugin,
                           final @NotNull String bankId) {
        super(20);
        this.bankManager = plugin.getBankManager();
        this.taskManager = plugin.getTaskManager();
        this.plugin = plugin;
        this.bankId = bankId;
    }

    /**
     * Calculates if a crisis should be executed. If true,
     * it will create {@link CrisisExecutorTask}
     */
    @Override
    public void compute() {

        final Crisis crisis = bankManager
                .getBank(bankId)
                .getCrisis();

        if (!crisis.shouldHappen()) {
            return;
        }

        taskManager.addTask(new CrisisExecutorTask(plugin, bankId));
    }

}
