package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

public class Limits {

    private final Double minimumBalance;
    private final Double maximumBalance;
    private final Integer withdrawInterval;

    public Limits(final @NotNull Double minimumBalance,
                  final @NotNull Double maximumBalance,
                  final @NotNull Integer withdrawInterval) {

        this.minimumBalance = minimumBalance;
        this.maximumBalance = maximumBalance;
        this.withdrawInterval = withdrawInterval;
    }

    /**
     * @return the minimum balance to open on this bank
     */
    public Double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * @return the maximum balance to open on this bank
     */
    public Double getMaximumBalance() {
        return maximumBalance;
    }

    /**
     * @return the withdrawal interval limit in seconds
     */
    public Integer getWithdrawInterval() {
        return withdrawInterval;
    }
}
