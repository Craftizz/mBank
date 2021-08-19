package io.github.craftizz.mbank.bank.user;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class User {

    private final UUID uniqueId;
    private final List<UserBankData> bankData;

    public User(final @NotNull UUID uniqueId,
                final @NotNull List<UserBankData> bankData) {
        this.uniqueId = uniqueId;
        this.bankData = bankData;
    }

    /**
     * Get the user's bankData of according to {@param bankId}
     *
     * @param bankId is the BankId to be queried
     * @return an optional userBankData
     */
    public @NotNull Optional<UserBankData> getUserBankData(final @NotNull Integer bankId) {
        return bankData.stream()
                .filter(bankData -> bankData.getBankId().equals(bankId))
                .findFirst();
    }

    /**
     * @return the uniqueId of the user from {@link org.bukkit.entity.Player}
     */
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * @return all the {@link UserBankData} of the User
     */
    public @NotNull List<UserBankData> getBankData() {
        return bankData;
    }

}
