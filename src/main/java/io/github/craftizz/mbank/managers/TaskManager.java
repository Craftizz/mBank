package io.github.craftizz.mbank.managers;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.tasks.CrisisTimerTask;
import io.github.craftizz.mbank.tasks.InterestTask;
import io.github.craftizz.mbank.tasks.TaskRunnable;
import io.github.craftizz.mbank.tasks.tasktypes.Task;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;


public class TaskManager {

    private final MBank plugin;
    private final TaskRunnable taskRunnable;

    public TaskManager(final @NotNull MBank plugin) {
        this.taskRunnable = new TaskRunnable();
        this.plugin = plugin;
    }

    /**
     * Adds the task on the {@link TaskRunnable}
     */
    public void startTask() {
        plugin.getBankManager().getBanks().forEach(bank -> {
            taskRunnable.addLoad(new InterestTask(plugin, bank.getId()));
            taskRunnable.addLoad(new CrisisTimerTask(plugin, bank.getId()));
        });

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, taskRunnable, 20, 20);
    }

    public void addTask(final @NotNull Task task) {
        taskRunnable.addLoad(task);
    }

    /**
     * @return the taskRunnable
     */
    public TaskRunnable getTaskRunnable() {
        return taskRunnable;
    }
}
