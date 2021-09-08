package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("mb")
@Alias("bank")
public class BankReloadCommand extends CommandBase {

    private final MBank plugin;

    public BankReloadCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
    }

    @SubCommand("reload")
    @Permission("mbank.admin.reload")
    public void onReloadCommand(final CommandSender commandSender) {
        commandSender.sendMessage("Plugin Reloaded");
        plugin.reload();
    }


}
