package io.github.craftizz.mbank.tasks.tasktypes;

import java.util.function.Predicate;

public abstract class ConditionalScheduledTask<T> implements Task, Predicate<T> {

    private final T element;

    public ConditionalScheduledTask(T element) {
        this.element = element;
    }

    @Override
    public boolean shouldReschedule() {
        return this.test(element);
    }


}
