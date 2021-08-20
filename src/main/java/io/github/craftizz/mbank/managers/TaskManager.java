package io.github.craftizz.mbank.managers;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.tasks.InterestTask;
import io.github.craftizz.mbank.tasks.TaskRunnable;
import org.jetbrains.annotations.NotNull;


public class TaskManager {

    private final MBank plugin;
    private final TaskRunnable taskRunnable;

    public TaskManager(final @NotNull MBank plugin) {
        this.taskRunnable = new TaskRunnable();
        this.plugin = plugin;
    }

    public void startInterest() {
        taskRunnable.addLoad(new InterestTask(plugin));
    }



    public TaskRunnable getTaskRunnable() {
        return taskRunnable;
    }
}
