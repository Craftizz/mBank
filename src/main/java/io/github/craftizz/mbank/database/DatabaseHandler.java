package io.github.craftizz.mbank.database;

import de.leonhard.storage.Json;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DatabaseHandler {

    private static final String dataPath = "plugins/mBank/data/users/";

    private final HashMap<UUID, Json> loadedUsersFiles;
    private final MBank plugin;

    public DatabaseHandler(final @NotNull MBank plugin) {
        this.loadedUsersFiles = new HashMap<>();
        this.plugin = plugin;
    }

    /**
     * Loads user from the database
     *
     * @param uniqueId is the uniqueId of the user to be loaded
     * @return the loaded {@link User}
     */
    public User loadUserByUniqueIdUrgently(final @NotNull UUID uniqueId) {

        final User user = new User(uniqueId);

        final FlatFileSection section = getOrLoadUserFile(uniqueId)
                .getSection(uniqueId.toString());

        for (final String bankId : section.singleLayerKeySet()) {

            final Double balance = section.getDouble(bankId + ".balance");
            final Double lostInLastCrisis = section.getDouble(bankId + ".lost-in-crisis");
            final Double totalInterestEarning = section.getDouble(bankId + ".total-interest-earning");
            final Double totalLostInCrisis = section.getDouble(bankId + ".total-lost-in-crisis");
            final String lastWithdraw = section.getString(bankId + ".last-withdraw");

            user.addBankData(new UserBankData(bankId, balance, lostInLastCrisis, totalInterestEarning, totalLostInCrisis, LocalDateTime.parse(lastWithdraw)));
        }

        return user;
    }

    /**
     * Loads user asynchronously with a timeout of 5 seconds, otherwise, it will load the user urgently
     *
     * @param uniqueId is the unique of the {@link User} to be loaded
     * @return the loaded user
     */
    public User loadUserByUniqueId(final @NotNull UUID uniqueId) {
        try {
            return CompletableFuture.supplyAsync(() -> loadUserByUniqueIdUrgently(uniqueId)).get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return loadUserByUniqueIdUrgently(uniqueId);
    }

    /**
     * Saves the user to the database
     *
     * @param user the user to be saved
     */
    public void saveUser(final @NotNull User user) {

        final Json userFile = getOrLoadUserFile(user.getUniqueId());
        final String uniqueId = user.getUniqueId().toString();

        for (final UserBankData bankData : user.getBankData()) {

            userFile.set(uniqueId + "." + bankData.getBankId() + ".balance", bankData.getBalance());
            userFile.set(uniqueId + "." + bankData.getBankId() + ".lost-in-crisis", bankData.getLostInLastCrisis());
            userFile.set(uniqueId + "." + bankData.getBankId() + ".total-interest-earning", bankData.getTotalInterestEarning());
            userFile.set(uniqueId + "." + bankData.getBankId() + ".total-lost-in-crisis", bankData.getTotalLostInCrisis());
            userFile.set(uniqueId + "." + bankData.getBankId() + ".last-withdraw", bankData.getLastWithdraw().toString());

        }

    }

    /**
     * Deletes a specific userBankData of a user on the database
     *
     * @param uniqueId the uniqueId to the {@link User}
     * @param bankId the Id of the bank to be removed
     */
    public void deleteUserBankData(final @NotNull UUID uniqueId,
                                   final @NotNull String bankId) {

        final Json userFile = getOrLoadUserFile(uniqueId);
        userFile.remove(uniqueId + "." + bankId);

    }

    /**
     * Saves a collection of user to the database
     *
     * @param userCollection the collection of users to be saved
     */
    public void saveUsers(Collection<User> userCollection) {
        for (User user : userCollection) {
            saveUser(user);
        }
    }

    public void unloadUnusedUserFile() {
        final Set<UUID> users = plugin.getUserManager().getUsers().keySet();
        loadedUsersFiles.keySet().removeIf(uuid -> !users.contains(uuid));
    }

    /**
     * Loads the User's JSON file
     *
     * @param uniqueId the uniqueId of the user to be loaded
     * @return the JSON file of the {@link User}
     */
    public Json getOrLoadUserFile(final @NotNull UUID uniqueId) {
        return Optional.ofNullable(loadedUsersFiles.get(uniqueId))
                .orElseGet(() -> {
                    final Json userFile = LightningBuilder
                            .fromPath(uniqueId.toString(), dataPath)
                            .createJson();
                    loadedUsersFiles.putIfAbsent(uniqueId, userFile);
                    return userFile;
                });
    }
}
