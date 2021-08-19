package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Interest {

    private final Double interest;
    private final Integer payoutInterval;

    private LocalDateTime nextPayout;

    public Interest(final @NotNull Double interest,
                    final @NotNull Integer payoutInterval) {
        this.interest = interest;
        this.payoutInterval = payoutInterval;
    }

    /**
     * calculates the nextPayout time considering {@param lastPayout}
     */
    public void calculateNextPayoutInterval(final @NotNull LocalDateTime lastPayout) {


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
     * @return the nextPayout interval of {@link Bank}
     */
    public @NotNull LocalDateTime getNextPayout() {
        return nextPayout;
    }
}
