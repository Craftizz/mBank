package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

public class Fees {

    private final Double depositFee;
    private final Double withdrawFee;

    public Fees(final @NotNull Double depositFee,
                final @NotNull Double withdrawFee) {
        this.depositFee = depositFee;
        this.withdrawFee = withdrawFee;
    }

    /**
     * Deposit fee is on percentage
     *
     * @return the deposit fee of {@link Bank}
     */
    public Double getDepositFee() {
        return depositFee;
    }

    /**
     * Withdrawal fee is on percentage
     *
     * @return the withdraw fee of {@link Bank}
     */
    public Double getWithdrawFee() {
        return withdrawFee;
    }
}
