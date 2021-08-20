package io.github.craftizz.mbank.bank;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Commands {

    private final List<String> joinCommands;
    private final List<String> leaveCommands;

    public Commands(final @NotNull List<String> joinCommands,
                    final @NotNull List<String> leaveCommands) {

        this.joinCommands = joinCommands;
        this.leaveCommands = leaveCommands;
    }

    /**
     * Executes all the join commands with {@param player} as placeholder
     */
    public void executeJoinCommands(final @NotNull Player player) {
        final ConsoleCommandSender console = Bukkit.getConsoleSender();
        joinCommands.forEach(command -> Bukkit.getServer()
                .dispatchCommand(console, command
                        .replace("<player>", player.getName())));
    }

    /**
     * Executes all the leave commands with {@param player} as placeholder
     */
    public void executeLeaveCommands(final @NotNull Player player) {
        final ConsoleCommandSender console = Bukkit.getConsoleSender();
        leaveCommands.forEach(command -> Bukkit.getServer()
                .dispatchCommand(console, command
                        .replace("<player>", player.getName())));
    }

    /**
     * @return the join commands for {@link Bank}
     */
    public List<String> getJoinCommands() {
        return joinCommands;
    }

    /**
     * @return the leave commands for {@link Bank}
     */
    public List<String> getLeaveCommands() {
        return leaveCommands;
    }
}
