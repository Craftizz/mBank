package io.github.craftizz.mbank;

import io.github.craftizz.mbank.database.DatabaseHandler;
import io.github.craftizz.mbank.hooks.VaultHook;
import io.github.craftizz.mbank.listeners.PlayerJoinListener;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MBank extends JavaPlugin {

    private DatabaseHandler databaseHandler;

    private VaultHook vaultHook;

    private UserManager userManager;
    private BankManager bankManager;

    @Override
    public void onEnable() {

        // Initialize Database and Configuration
        this.databaseHandler = new DatabaseHandler();

        // Initialize Hooks
        this.vaultHook = new VaultHook();
        vaultHook.checkIfVaultExist(this);
        vaultHook.setupEconomy(this);
        vaultHook.setupPermission(this);

        // Initialize Managers
        this.userManager = new UserManager(this);
        this.bankManager = new BankManager(this);

        // Initialize Listeners
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(this), this);

        // Initialize Commands


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public BankManager getBankManager() {
        return bankManager;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }
}
