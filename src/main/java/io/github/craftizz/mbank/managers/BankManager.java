package io.github.craftizz.mbank.managers;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.database.DatabaseHandler;
import io.github.craftizz.mbank.hooks.VaultHook;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class BankManager {

    private final UserManager userManager;
    private final DatabaseHandler databaseHandler;
    private final HashMap<String, Bank> banks;

    public BankManager(final @NotNull MBank plugin) {
        this.userManager = plugin.getUserManager();
        this.databaseHandler = plugin.getDatabaseHandler();
        this.banks = new HashMap<>();
    }

    /**
     * Get the balance of a player from a specific bank
     *
     * @param bank the bank where money will be deposited
     * @param offlinePlayer the player to be queried
     * @return the balance
     */
    public Double getBalance(final @NotNull Bank bank,
                             final @NotNull OfflinePlayer offlinePlayer) {
        Optional<UserBankData> bankData = userManager
                .getUser(offlinePlayer)
                .getUserBankData(bank.getId());
        return bankData.map(UserBankData::getBalance).orElse(0d);
    }

    /**
     * Deposits money into a bank
     *
     * @param bank the bank where amount will be deposited
     * @param offlinePlayer the player to be deposited
     * @param amount the amount to be deposited
     */
    public void deposit(final @NotNull Bank bank,
                        final @NotNull OfflinePlayer offlinePlayer,
                        final @NotNull Double amount) {

        final Optional<UserBankData> bankData = userManager
                .getUser(offlinePlayer)
                .getUserBankData(bank.getId());

        bankData.ifPresent(userBankData -> {
            final double amountWithFee = amount - bank.getFees().calculateDepositFee(amount);
            VaultHook.getEconomy().withdrawPlayer(offlinePlayer, amount);
            userBankData.deposit(amountWithFee);
        });
    }

    /**
     * Withdraws money from a bank
     *
     * @param bank the bank where the amount will be withdrawn
     * @param offlinePlayer the player to be withdrawn
     * @param amount the amount to be withdrawn
     */
    public void withdraw(final @NotNull Bank bank,
                         final @NotNull OfflinePlayer offlinePlayer,
                         final @NotNull Double amount) {

        final Optional<UserBankData> bankData = userManager
                .getUser(offlinePlayer)
                .getUserBankData(bank.getId());

        bankData.ifPresent(userBankData -> {
            final double amountWithFee = amount - bank.getFees().calculateWithdrawFee(amount);
            VaultHook.getEconomy().depositPlayer(offlinePlayer, amountWithFee);
            userBankData.withdraw(amount);
            userBankData.setLastWithdraw(LocalDateTime.now());
        });
    }

    /**
     * Creates a new bank account for {@param offlinePlayer}
     *
     * @param bank the bank where the account will be created
     * @param offlinePlayer the player requesting
     */
    public void createAccount(final @NotNull Bank bank,
                              final @NotNull OfflinePlayer offlinePlayer) {
        userManager.getUser(offlinePlayer).createBankData(bank.getId());
        bank.getCommands().executeJoinCommands(offlinePlayer);
    }

    /**
     * Removes a bank account of offlinePlayer
     *
     * @param bank the bank where the account will be deleted
     * @param offlinePlayer the player requesting
     */
    public void deleteAccount(final @NotNull Bank bank,
                              final @NotNull OfflinePlayer offlinePlayer) {
        userManager.getUser(offlinePlayer).removeBankData(bank.getId());
        databaseHandler.deleteUserBankData(offlinePlayer.getUniqueId(), bank.getId());
        bank.getCommands().executeLeaveCommands(offlinePlayer);
    }

    /**
     * Checks if player has a bank account
     *
     * @param bank the bank to be checked
     * @param offlinePlayer the player to be queried
     * @return player has an account on the bank
     */
    public boolean hasAccount(final @NotNull Bank bank,
                              final @NotNull OfflinePlayer offlinePlayer) {
        return userManager.getUser(offlinePlayer).hasBankData(bank.getId());
    }

    /**
     * Adds {@param bank} to the banks hashmap
     *
     * @param bank the bank to be added
     */
    public void addBank(final @NotNull Bank bank) {
        banks.put(bank.getId(), bank);
    }

    /**
     * Get the bank using the bankId
     *
     * @param bankId the id of the bank to be queried
     * @return the bank, or null
     */
    public Bank getBank(final @NotNull String bankId) {
        return banks.get(bankId);
    }

    public void clearBankMap() {
        banks.clear();
    }

    /**
     * @return the map of banks
     */
    public HashMap<String, Bank> getBankMap() {
        return banks;
    }

    public Collection<Bank> getBanks() {
        return banks.values();
    }
}
