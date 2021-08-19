package io.github.craftizz.mbank;

import io.github.craftizz.mbank.hooks.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;

public final class MBank extends JavaPlugin {

    private VaultHook vaultHook;

    @Override
    public void onEnable() {

        // Initialize Hooks
        this.vaultHook = new VaultHook(this);


        // Initialize Managers


        // Initialize Managers


        // Initialize Commands
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }
}
