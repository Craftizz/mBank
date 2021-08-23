package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.configuration.ConfigurationHandler;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command("mb")
@Alias("bank")
public class BankStatisticsCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;
    private final ConfigurationHandler configurationHandler;

    public BankStatisticsCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
        this.configurationHandler = plugin.getConfigurationHandler();
    }

    @SubCommand("current")
    @Permission("mbank.banks.gui.open")
    public void onBankCurrentCommand(final @NotNull Player player,
                                     final @NotNull @Completion("#banks") String bankName) {

        final Bank bank = bankManager.getBank(bankName);
        final User user = userManager.getUser(player);

        // Check if bank exists
        if (bank == null) {
            MessageUtil.sendMessage(player,
                    Language.NONEXISTENT_BANK,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        final Optional<UserBankData> optionalUserBankData = user.getUserBankData(bankName);

        // Check if player is a bankUser of the bank
        if (optionalUserBankData.isEmpty()) {
            MessageUtil.sendMessage(player,
                    Language.NOT_IN_BANK,
                    MessageType.DENY,
                    "bank", bankName);
            return;
        }

        plugin.getGuiManager().openBankStatisticsGUI(player, optionalUserBankData.get(), bank, configurationHandler.getGuiYaml());
    }

}
