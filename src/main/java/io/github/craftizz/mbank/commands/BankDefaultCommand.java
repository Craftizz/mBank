package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.utils.MessageUtil;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Command("mb")
@Alias("bank")
public class BankDefaultCommand extends CommandBase {

    private final MBank plugin;

    public BankDefaultCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
    }

    @Default
    @SubCommand("help")
    public void onBankHelpCommand(final @NotNull Player player) {

        final List<Component> messages = MessageUtil.parseMany(plugin
                .getConfigurationHandler()
                .getLanguage()
                .getStringList("help"));

        for (final Component message : messages) {
            player.sendMessage(message);
        }

    }

}
