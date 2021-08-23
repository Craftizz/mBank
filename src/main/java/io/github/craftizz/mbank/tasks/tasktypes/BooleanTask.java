package io.github.craftizz.mbank.tasks.tasktypes;

public abstract class BooleanTask implements Task {

    private boolean shouldReschedule = true;

    @Override
    public boolean shouldReschedule() {
        return shouldReschedule;
    }

    public void setShouldReschedule(boolean shouldReschedule) {
        this.shouldReschedule = shouldReschedule;
    }
}
