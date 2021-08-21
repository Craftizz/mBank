package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Optional;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("mb")
@Alias("bank")
public class BankBalanceCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankBalanceCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("balance")
    public void onBankBalanceCommand(final @NotNull Player player) {

        final User user = userManager.getUser(player);

        // Check if has a bank data
        if (user.getBankData().isEmpty()) {
            MessageUtil.sendMessage(player,
                    Language.DO_NOT_HAVE_BANK,
                    MessageType.DENY);
            return;
        }

        // Get the total balance
        final Double totalBalance = user.getBankData()
                .stream()
                .mapToDouble(UserBankData::getBalance)
                .sum();

        // Send message to player
        MessageUtil.sendMessage(player,
                Language.BANK_BALANCE,
                MessageType.INFORMATION,
                "amount", NumberUtils.formatCurrency(totalBalance));
    }
}
