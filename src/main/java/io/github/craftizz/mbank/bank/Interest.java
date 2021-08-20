package io.github.craftizz.mbank.bank;

import io.github.craftizz.mbank.bank.user.UserBankData;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Interest {

    private final Double interest;
    private final Integer payoutInterval;

    private Integer timeLeft;

    public Interest(final @NotNull Double interest,
                    final @NotNull Integer payoutInterval) {
        this.interest = interest;
        this.payoutInterval = payoutInterval;
        this.timeLeft = payoutInterval;
    }

    /**
     * @return if the bank should give Interest
     */
    public boolean shouldGiveInterest() {
        return timeLeft-- <= 0;
    }

    /**
     * Calculates the next interest LocalDateTime
     */
    public void calculateNextPayout() {
        this.timeLeft += payoutInterval;
    }

    /**
     * Set the timeLeft
     *
     * @param timeLeft the new timeLeft
     */
    public void setTimeLeft(@NotNull Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * Calculates interest according to the balance and interest
     *
     * @param balance the balance to be calculated
     * @return the interest
     */
    public double calculateInterest(final @NotNull Double balance) {
        return balance * this.interest;
    }

    /**
     * @return the interest rate of the {@link Bank}
     */
    public @NotNull Double getInterest() {
        return interest;
    }

    /**
     * @return the payout interval of the {@link Bank}
     */
    public @NotNull Integer getPayoutInterval() {
        return payoutInterval;
    }

    /**
     * @return the nextPayout interval of {@link Bank} in seconds
     */
    public @NotNull Integer getNextPayout() {
        return this.timeLeft;
    }

    /**
     * @return the nextPayout interval of {@link Bank} in localDateTime
     */
    public @NotNull LocalDateTime getNextPayoutInLocalDateTime() {
        return LocalDateTime.now().plusSeconds(timeLeft);
    }
}
