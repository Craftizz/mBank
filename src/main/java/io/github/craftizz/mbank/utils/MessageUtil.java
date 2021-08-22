package io.github.craftizz.mbank.utils;

import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

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
    public static Component parse(final @NotNull String message) {
        return miniMessage.parse(message)
                .decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Use to parse languages for GUIs with the placeholders as parsed
     *
     * @param message the string message to be parsed
     * @return the parsed language in component
     */
    public static Component parse(final @NotNull String message,
                                  final @NotNull String... placeholders) {
        return miniMessage.parse(message, placeholders)
                .decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Use to parse many language for GUIs
     *
     * @param messages the list of string messages to be parsed
     * @return the parsed languages in component
     */
    public static List<Component> parseMany(List<String> messages) {
        return messages.stream().map(MessageUtil::parse).collect(Collectors.toList());
    }

    /**
     * Use to parse many language for GUIs with placeholders as parsed
     *
     * @param messages the list of string messages to be parsed
     * @param placeholders the text values and placeholders to be parsed
     * @return the parsed languages in component
     */
    public static List<Component> parseMany(List<String> messages, String... placeholders) {
        return messages.stream().map(message -> parse(message, placeholders)).collect(Collectors.toList());
    }
}
