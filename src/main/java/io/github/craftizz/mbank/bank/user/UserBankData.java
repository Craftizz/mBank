package io.github.craftizz.mbank.bank.user;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class UserBankData {

    private final Integer bankId;
    private final Double balance;
    private final LocalDateTime lastWithdraw;

    public UserBankData(final @NotNull Integer bankId,
                        final @NotNull Double balance,
                        final @NotNull LocalDateTime lastWithdraw) {
        this.bankId = bankId;
        this.balance = balance;
        this.lastWithdraw = lastWithdraw;
    }

    /**
     * @return the id of {@link io.github.craftizz.mbank.bank.Bank}
     */
    public @NotNull Integer getBankId() {
        return bankId;
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

}
