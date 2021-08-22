package io.github.craftizz.mbank.utils;

import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtil {

    public static MiniMessage miniMessage = MiniMessage.get();

    /**
     * Sends a player a message
     *
     * @param player the player to be sent the message
     * @param language the language to be sent
     * @param messageType type of message
     */
    public static void sendMessage(final @NotNull Player player,
                                   final @NotNull Language language,
                                   final @NotNull MessageType messageType) {

        player.sendMessage(miniMessage.parse(language.getMessage()));
        player.playSound(player.getLocation(), messageType.getSound(), 1, 1);
    }

    /**
     * Sends a message to an unknown type of command sender
     *
     * @param commandSender the command sender to be sent the message
     * @param language the language to be sent
     */
    public static void sendMessage(final @NotNull CommandSender commandSender,
                                   final @NotNull Language language) {
        commandSender.sendMessage(miniMessage.parse(language.getMessage()));
    }

    /**
     * Sends a player a message with the placeholders as parsed
     *
     * @param player the player to be sent the message
     * @param language the language to be sent
     * @param messageType type of message
     * @param placeholders the placeholder to be parsed
     */
    public static void sendMessage(final @NotNull Player player,
                                   final @NotNull Language language,
                                   final @NotNull MessageType messageType,
                                   final @NotNull String... placeholders) {
        player.sendMessage(miniMessage.parse(language.getMessage(), placeholders));
        player.playSound(player.getLocation(), messageType.getSound(), 1, 1);
    }

    /**
     * Use to parse languages for GUIs
     *
     * @param message the string message to be parsed
     * @return the parsed language in component
     */
    public static Component parse(String message) {
        return miniMessage.parse(message)
                .decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Use to parse languages for GUIs with the placeholders as parsed
     *
     * @param message the string message to be parsed
     * @return the parsed language in component
     */
    public static Component parse(String message, String... placeholders) {
        return miniMessage.parse(message, placeholders)
                .decoration(TextDecoration.ITALIC, false);
    }
}
