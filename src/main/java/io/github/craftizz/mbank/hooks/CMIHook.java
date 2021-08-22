package io.github.craftizz.mbank.hooks;

import com.Zrips.CMI.CMI;
import io.github.craftizz.mbank.MBank;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class CMIHook {

    private static CMI cmi;

    /**
     * Setups the CMI Hook
     *
     * @param plugin is the plugin instance
     */
    public void setupInstance(final @NotNull MBank plugin) {

        final PluginManager manager = plugin.getServer().getPluginManager();

        if (manager.getPlugin("CMI") == null) {
            manager.disablePlugin(plugin);
        }

        cmi = CMI.getInstance();
    }

    public static CMI getCmi() {
        return cmi;
    }
}
