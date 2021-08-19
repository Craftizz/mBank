package io.github.craftizz.mbank.hooks;

import io.github.craftizz.mbank.MBank;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook {

    private static Economy economy;
    private static Permission permission;

    public VaultHook(final @NotNull MBank plugin) {
        if (!checkIfVaultExist(plugin)) disablePlugin(plugin);
        if (!setupEconomy(plugin)) disablePlugin(plugin);
        if (!setupPermission(plugin)) disablePlugin(plugin);
    }

    /**
     * Setups the {@link Economy}
     *
     * @param plugin is the instance of {@link MBank}
     * @return if the setup was a success
     */
    public boolean setupEconomy(final @NotNull MBank plugin) {

        final RegisteredServiceProvider<Economy> rsp = plugin.getServer()
                .getServicesManager()
                .getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

    /**
     * Setups the {@link Permission}
     *
     * @param plugin is the instance of {@link MBank}
     * @return if the setup was a success
     */
    public boolean setupPermission(final @NotNull MBank plugin) {

        RegisteredServiceProvider<Permission> rsp = plugin.getServer()
                .getServicesManager()
                .getRegistration(Permission.class);

        if (rsp == null) {
            return false;
        }

        permission = rsp.getProvider();
        return true;
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
