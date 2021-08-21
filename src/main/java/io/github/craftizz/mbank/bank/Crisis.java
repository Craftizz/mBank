package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.SplittableRandom;

public class Crisis {

    private static final SplittableRandom random = new SplittableRandom();

    private final Double chanceToHappen;
    private final Double chanceToLose;
    private final Double minimumLostInPercentage;
    private final Double maximumLostInPercentage;
    private final Integer interval;

    private Integer timeLeft;

    public Crisis(final @NotNull Double chanceToHappen,
                  final @NotNull Double chanceToLose,
                  final @NotNull Double minimumLostInPercentage,
                  final @NotNull Double maximumLostInPercentage,
                  final @NotNull Integer interval) {

        this.chanceToHappen = chanceToHappen;
        this.chanceToLose = chanceToLose;
        this.minimumLostInPercentage = minimumLostInPercentage;
        this.maximumLostInPercentage = maximumLostInPercentage;
        this.interval = interval;
        this.timeLeft = interval;
    }

    /**
     * @return if the crisis should happen according to the time and chance
     */
    public boolean shouldHappen() {
        if (timeLeft-- > 0) {
            return false;
        }

        calculateNextCrisis();
        return !(Math.random() >= chanceToHappen);
    }

    /**
     * Calculates the amount to be taken from the balance during a crisis.
     * This will respect the maximum and minimum lost
     *
     * @param balance is the balance to be calculated
     * @return the money to be withdrawn
     */
    public Double calculateLost(final @NotNull Double balance) {
        if (Math.random() <= chanceToLose) {
            return 0d;
        }
        return balance * (random.nextDouble(maximumLostInPercentage, minimumLostInPercentage));
    }

    /**
     * Calculates the next crisis
     */
    public void calculateNextCrisis() {
        this.timeLeft += interval;
    }

    /**
     * Sets the next crisis
     *
     * @param timeLeft is the time left in seconds before the next crisis
     */
    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * @return the chance to happen of a crisis
     */
    public Double getChanceToHappen() {
        return chanceToHappen;
    }

    /**
     * @return the chance to lose money during a crisis
     */
    public Double getChanceToLose() {
        return chanceToLose;
    }

    /**
     * @return the minimum percentage of the overall user's balance that can be lost
     */
    public Double getMinimumLostInPercentage() {
        return minimumLostInPercentage;
    }

    /**
     * @return the maximum percentage of the overall user's balance that can be lost
     */
    public Double getMaximumLostInPercentage() {
        return maximumLostInPercentage;
    }

    /**
     * @return how often the crisis will happen
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * @return the next crisis interval in seconds
     */
    public Integer getNextCrisis() {
        return timeLeft;
    }

    /**
     * @return the next crisis interval in LocalDateTime
     */
    public LocalDateTime getNextCrisisInLocalDateTime() {
        return LocalDateTime.now().plusSeconds(timeLeft);
    }
}
