package io.github.craftizz.mbank.managers;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.tasks.CrisisTimerTask;
import io.github.craftizz.mbank.tasks.InterestTask;
import io.github.craftizz.mbank.tasks.TaskRunnable;
import io.github.craftizz.mbank.tasks.tasktypes.Task;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;


public class TaskManager {

    private final MBank plugin;
    private final TaskRunnable taskRunnable;

    private int taskId;

    public TaskManager(final @NotNull MBank plugin) {
        this.taskRunnable = new TaskRunnable();
        this.plugin = plugin;
    }

    /**
     * Adds the task on the {@link TaskRunnable}
     */
    public void startTask() {
        createBankTasks();
        this.taskId = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(plugin, taskRunnable, 1, 1)
                .getTaskId();
    }

    public void createBankTasks() {
        for (final Bank bank : plugin.getBankManager().getBanks()) {
            taskRunnable.addLoad(new InterestTask(plugin, bank.getId()));
            taskRunnable.addLoad(new CrisisTimerTask(plugin, bank.getId()));
        }
    }

    public void stopBankTask() {
        Bukkit.getScheduler().cancelTask(taskId);
        taskRunnable.stopTasks();
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
