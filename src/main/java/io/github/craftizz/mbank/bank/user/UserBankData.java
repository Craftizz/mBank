package io.github.craftizz.mbank.bank.user;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class UserBankData {

    private final String bankId;

    private LocalDateTime lastWithdraw;
    private Double balance;

    public UserBankData(final @NotNull String bankId,
                        final @NotNull Double balance,
                        final @NotNull LocalDateTime lastWithdraw) {
        this.bankId = bankId;
        this.balance = balance;
        this.lastWithdraw = lastWithdraw;
    }

    /**
     * @return the id of {@link io.github.craftizz.mbank.bank.Bank}
     */
    public @NotNull String getBankId() {
        return bankId;
    }

    /**
     * @param amount to be deposited
     */
    public void deposit(final @NotNull Double amount) {
        this.balance += amount;
    }

    /**
     * @param amount to be withdrawn
     */
    public void withdraw(final @NotNull Double amount) {
        this.balance -= amount;
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
}
