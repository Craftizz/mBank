package io.github.craftizz.mbank.hooks;

import io.github.craftizz.mbank.MBank;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook {

    private static Economy economy;
    private   static Permission permission;

    /**
     * Setups the {@link Economy}
     *
     * @param plugin is the instance of {@link MBank}
     */
    public void setupEconomy(final @NotNull MBank plugin) {

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer()
                .getServicesManager()
                .getRegistration(Economy.class);

        if (rsp == null) {
            disablePlugin(plugin);
            return;
        }

        economy = rsp.getProvider();
    }

    /**
     * Setups the {@link Permission}
     *
     * @param plugin is the instance of {@link MBank}
     */
    public void setupPermission(final @NotNull MBank plugin) {

        RegisteredServiceProvider<Permission> rsp = plugin.getServer()
                .getServicesManager()
                .getRegistration(Permission.class);

        if (rsp == null) {
            disablePlugin(plugin);
            return;
        }

        permission = rsp.getProvider();
    }

    /**
     * Checks if Vault exist
     *
     * @param plugin is the instance of {@link MBank}
     * @return if Vault exists
     */
    public boolean checkIfVaultExist(final @NotNull MBank plugin) {
        return plugin.getServer().getPluginManager().getPlugin("Vault") == null;
    }

    /**
     * Disables {@link MBank} for safety
     */
    public void disablePlugin(final @NotNull MBank plugin) {
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    /**
     * @return the {@link Economy}
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * @return the {@link Permission}
     */
    public static Permission getPermission() {
        return permission;
    }

}
