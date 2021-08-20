package io.github.craftizz.mbank.managers;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.tasks.InterestTask;
import io.github.craftizz.mbank.tasks.TaskRunnable;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;


public class TaskManager {

    private final MBank plugin;
    private final TaskRunnable taskRunnable;

    public TaskManager(final @NotNull MBank plugin) {
        this.taskRunnable = new TaskRunnable();
        this.plugin = plugin;
    }

    public void startTask() {
        taskRunnable.addLoad(new InterestTask(plugin));
        // Add Other Task

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, taskRunnable, 20, 20);
    }

    public TaskRunnable getTaskRunnable() {
        return taskRunnable;
    }
}
