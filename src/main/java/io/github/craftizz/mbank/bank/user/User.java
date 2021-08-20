package io.github.craftizz.mbank.bank.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID uniqueId;
    private final List<UserBankData> bankData;

    public User(final @NotNull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.bankData = new ArrayList<>();
    }

    /**
     * Get the user's bankData of according to {@param bankId}
     *
     * @param bankId is the BankId to be queried
     * @return an optional userBankData
     */
    public UserBankData getUserBankData(final @NotNull String bankId) {
        return bankData.stream()
                .filter(bankData -> bankData.getBankId().equals(bankId))
                .findFirst()
                .orElseGet(() -> createBankData(bankId));
    }

    /**
     * Create a new bankData
     *
     * @param bankId the id of the bank
     * @return the new bankData
     */
    public UserBankData createBankData(final @NotNull String bankId) {
        final UserBankData userBankData = new UserBankData(bankId, 0d, LocalDateTime.now());
        bankData.add(userBankData);
        return userBankData;
    }

    /**
     * Checks if user has a bankData
     *
     * @param bankId the bankId to be queried
     * @return if player has a bank account
     */
    public boolean hasBankData(final @NotNull String bankId) {
        for (final UserBankData bankData : bankData) {
            if (bankData.getBankId().equals(bankId)) return true;
        }
        return false;
    }


    /**
     * Removes the userBankData
     *
     * @param bankId the bankId to be removed
     */
    public void removeBankData(final @NotNull String bankId) {
        bankData.removeIf(userBankData -> userBankData.getBankId().equals(bankId));
    }

    /**
     * @return the {@link OfflinePlayer} of this user
     */
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    /**
     * @return the {@link Player} of this user
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    /**
     * Adds a userBankData to the user's list
     *
     * @param userBankData the bankData to be added
     */
    public void addBankData(final @NotNull UserBankData userBankData) {
        bankData.add(userBankData);
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
