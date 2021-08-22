package io.github.craftizz.mbank.commands;

import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.*;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Command("mb")
@Alias("bank")
public class BankInfoCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    public BankInfoCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
    }

    @SubCommand("info")
    public void onBankInfoCommand(final @NotNull Player player,
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

        final Restrictions restrictions = bank.getRestrictions();
        final Interest interest = bank.getInterest();
        final Crisis crisis = bank.getCrisis();
        final Fees fees = bank.getFees();

        final List<Component> messages = MessageUtil.parseMany(plugin.getConfigurationHandler().getLanguage().getStringList("bank-info"),
                "bank", bankName,
                "minimumbalance", NumberUtils.formatCurrency(restrictions.getMinimumBalance()),
                "maximumbalance", NumberUtils.formatCurrency(restrictions.getMaximumBalance()),
                "deposit", String.valueOf(fees.getDepositFee() * 100),
                "withdraw", String.valueOf(fees.getWithdrawFee() * 100),
                "delay", NumberUtils.convertSeconds(restrictions.getWithdrawInterval()),
                "interest", String.valueOf(interest.getInterest() * 100),
                "interestleft", NumberUtils.convertSeconds(interest.getNextPayout()),
                "crisischance",String.valueOf(crisis.getChanceToHappen() * 100),
                "crisisleft", NumberUtils.convertSeconds(crisis.getNextCrisis()),
                "minimum", String.valueOf(crisis.getMinimumLostInPercentage() * 100),
                "maximum", String.valueOf(crisis.getMaximumLostInPercentage() * 100));

        for (final Component component : messages) {
            player.sendMessage(component);
        }
    }
}
