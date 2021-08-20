package io.github.craftizz.mbank.tasks.tasktypes;

public abstract class TimedTask extends ConditionalScheduledTask<Integer> {

    private int secondsChecked;

    public TimedTask(Integer secondsPerExecution) {
        super(secondsPerExecution);
        secondsChecked = 0;
    }

    @Override
    public boolean test(Integer secondsPerExecution) {
        return secondsChecked++ % secondsPerExecution == 0;
    }
}
