package io.github.craftizz.mbank.tasks.tasktypes;

public interface Task {

    void compute();

    default boolean shouldReschedule() {
        return false;
    }

    boolean reschedule();
}