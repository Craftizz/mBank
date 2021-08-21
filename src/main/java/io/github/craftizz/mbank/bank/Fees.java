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
     * Calculates the deposit fee of the amount
     *
     * @param amount the amount where the deposit fee will be calculated
     * @return the deposit fee
     */
    public Double calculateDepositFee(final @NotNull Double amount) {
        return amount * depositFee;
    }

    /**
     * Calculates the withdraw fee of the amount
     *
     * @param amount the amount where the withdraw fee will be calculated
     * @return the withdraw fee
     */
    public Double calculateWithdrawFee(final @NotNull Double amount) {
        return amount * withdrawFee;
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
