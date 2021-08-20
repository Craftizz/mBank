package io.github.craftizz.mbank.listeners;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.managers.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final UserManager userManager;

    public PlayerJoinListener(final @NotNull MBank plugin) {
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        userManager.loadUser(event.getPlayer().getUniqueId());
    }
}
