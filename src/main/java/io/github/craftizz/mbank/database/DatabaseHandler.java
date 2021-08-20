package io.github.craftizz.mbank.database;

import de.leonhard.storage.Json;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseHandler {

    private static final String dataPath = "plugins/mBank/data/users/";

    private final HashMap<UUID, Json> loadedUsersFiles;

    public DatabaseHandler() {
        this.loadedUsersFiles = new HashMap<>();
    }

    /**
     * Loads user from the database
     *
     * @param uniqueId is the uniqueId of the user to be loaded
     * @return the loaded {@link User}
     */
    public User loadUserByUniqueId(final @NotNull UUID uniqueId) {

        final User user = new User(uniqueId);

        final FlatFileSection section = getOrLoadUserFile(uniqueId)
                .getSection(uniqueId.toString());

        for (final String key : section.singleLayerKeySet()) {

            final int bankId = Integer.parseInt(key);
            final double balance = section.getDouble("balance");
            final String lastWithdraw = section.getString("last-withdraw");

            user.addBankData(new UserBankData(bankId, balance, LocalDateTime.parse(lastWithdraw)));
        }

        return user;
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
            userFile.set(uniqueId + "." + bankData.getBankId() + ".last-withdraw", bankData.getLastWithdraw().toString());

        }

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
