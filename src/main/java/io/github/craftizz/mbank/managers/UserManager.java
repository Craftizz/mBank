package io.github.craftizz.mbank.managers;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.database.DatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

public class UserManager {

    private final MBank plugin;
    private final HashMap<UUID, User> users;

    public UserManager(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.users = new HashMap<>();
    }

    /**
     * Get the user from the cachedUsers. If missing, we will
     * attempt to load it from the database.
     *
     * @param uniqueId is the UniqueId of the user
     * @return the cachedUser
     */
    public User getUser(final @NotNull UUID uniqueId) {
        final Optional<User> user = Optional.ofNullable(users.get(uniqueId));
        return user.orElseGet(() -> {
            final User loadedUser = plugin.getDatabaseHandler().loadUserByUniqueIdUrgently(uniqueId);
            users.put(uniqueId, loadedUser);
            return loadedUser;
        });
    }

    public User getUser(final @NotNull Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(final @NotNull OfflinePlayer offlinePlayer) {
        return getUser(offlinePlayer.getUniqueId());
    }

    /**
     * Loads user to the database. This uses an asynchronous method.
     *
     * @param uniqueId the uniqueId of the user
     * @return the cachedUser
     */
    public User loadUser(final @NotNull UUID uniqueId) {
        final Optional<User> user = Optional.ofNullable(users.get(uniqueId));
        return user.orElseGet(() -> {
            final User loadedUser = plugin.getDatabaseHandler().loadUserByUniqueId(uniqueId);
            users.put(uniqueId, loadedUser);
            return loadedUser;
        });
    }

    /**
     * Unloads the user from the cache
     *
     * @param uniqueId the UUID of {@link User}
     */
    public void unloadUser(final @NotNull UUID uniqueId) {
        users.remove(uniqueId);
    }

    public void unloadUser(final @NotNull User user) {
        users.remove(user.getUniqueId());
    }

    /**
     * Saves user to the database asynchronously
     *
     * @param user the user to be saved
     */
    public void saveUser(final @NotNull User user) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabaseHandler().saveUser(user));
    }

    /**
     * @return the hashmap of users
     */
    public HashMap<UUID, User> getUsers() {
        return users;
    }

    /**
     * Saves all of the loaded users to the database asynchronously
     */
    public void saveAllUsers() {

        final DatabaseHandler databaseHandler = plugin.getDatabaseHandler();
        final Iterator<User> userIterator = users.values().iterator();

        while (userIterator.hasNext()) {

            final User user = userIterator.next();
            databaseHandler.saveUser(user);

            if (user.getPlayer() == null) {
                userIterator.remove();
            }
        }

        databaseHandler.unloadUnusedUserFile();
    }

    /**
     * Starts saving all users. Used on {@link MBank#onEnable()}
     */
    public void startSaving() {
        plugin.getLogger().warning("Saving Users Bank Data...");
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllUsers, 600, 600);
    }

}
