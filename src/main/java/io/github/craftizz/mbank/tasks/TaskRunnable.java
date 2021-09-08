package io.github.craftizz.mbank.tasks;

import com.google.common.collect.Queues;
import io.github.craftizz.mbank.tasks.tasktypes.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;

public class TaskRunnable implements Runnable {

    private static final int MAX_MS_PER_SECOND = 5;
    private final ArrayDeque<Task> taskDeque;

    private Task firstTask;

    public TaskRunnable() {
        this.taskDeque = Queues.newArrayDeque();
    }

    public void addLoad(final @NotNull Task task) {
        taskDeque.add(task);
    }

    private boolean computeTask(final @Nullable Task task) {
        if (task != null) {
            if (task.shouldExecute()) {
                task.compute();
            }
            if (task.shouldReschedule()) {
                addLoad(task);
                if (firstTask == null) {
                    firstTask = task;
                } else {
                    return firstTask != task;
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        final long stopTime = System.currentTimeMillis() + MAX_MS_PER_SECOND;

        while (!taskDeque.isEmpty() && System.currentTimeMillis() <= stopTime) {
            final Task task = taskDeque.poll();

            if (!computeTask(task)) {
                break;
            }
        }
    }

    public void stopTasks() {
        taskDeque.clear();
    }
}
