package io.github.craftizz.mbank.bank.user;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class UserBankData {

    private final String bankId;
    private LocalDateTime lastWithdraw;

    private Double balance;
    private Double lostInLastCrisis;
    private Double totalInterestEarning;
    private Double totalLostInCrisis;

    public UserBankData(final @NotNull String bankId,
                        final @NotNull Double balance,
                        final @NotNull Double lostInLastCrisis,
                        final @NotNull Double totalInterestEarning,
                        final @NotNull Double totalLostInCrisis,
                        final @NotNull LocalDateTime lastWithdraw) {
        this.bankId = bankId;
        this.balance = balance;
        this.lostInLastCrisis = lostInLastCrisis;
        this.totalInterestEarning = totalInterestEarning;
        this.totalLostInCrisis = totalLostInCrisis;
        this.lastWithdraw = lastWithdraw;
    }

    /**
     * @return the id of {@link io.github.craftizz.mbank.bank.Bank}
     */
    public @NotNull String getBankId() {
        return bankId;
    }

    /**
     * Adds amount to the balance
     *
     * @param amount to be deposited
     */
    public void deposit(final @NotNull Double amount) {
        this.balance += amount;
    }

    /**
     * Removes amount from the balance
     *
     * @param amount to be withdrawn
     */
    public void withdraw(final @NotNull Double amount) {
        this.balance -= amount;
    }

    /**
     * Adds amount to the total interest gain
     *
     * @param amount the amount to be added
     */
    public void addToTotalInterest(final @NotNull Double amount) {
        this.totalInterestEarning += amount;
    }

    /**
     * Adds amount to the total lost in crisis
     *
     * @param amount the amount to be added
     */
    public void addToTotalLostInCrisis(final @NotNull Double amount) {
        this.totalLostInCrisis += amount;
    }

    /**
     * @return the balance of the user in the bank
     */
    public @NotNull Double getBalance() {
        return balance;
    }

    /**
     * @return the last withdraw localDateTime in the bank
     */
    public @NotNull LocalDateTime getLastWithdraw() {
        return lastWithdraw;
    }

    /**
     * Sets the last withdraw to {@param lastWithdraw}
     */
    public void setLastWithdraw(LocalDateTime lastWithdraw) {
        this.lastWithdraw = lastWithdraw;
    }

    public void setLostInLastCrisis(Double lostInLastCrisis) {
        this.lostInLastCrisis = lostInLastCrisis;
    }

    /**
     * @return the last lost that the {@link User} lost in the
     * last bank crisis
     */
    public Double getLostInLastCrisis() {
        return lostInLastCrisis;
    }

    public Double getTotalInterestEarning() {
        return totalInterestEarning;
    }

    public Double getTotalLostInCrisis() {
        return totalLostInCrisis;
    }
}
