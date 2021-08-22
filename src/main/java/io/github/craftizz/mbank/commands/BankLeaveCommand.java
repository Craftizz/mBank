package io.github.craftizz.mbank.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.configuration.MessageType;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Command("mb")
@Alias("bank")
public class BankLeaveCommand extends CommandBase {

    private final MBank plugin;
    private final BankManager bankManager;
    private final UserManager userManager;

    private final Cache<UUID, String> leaveRequests;

    public BankLeaveCommand(final @NotNull MBank plugin) {
        this.plugin = plugin;
        this.bankManager = plugin.getBankManager();
        this.userManager = plugin.getUserManager();
        this.leaveRequests = CacheBuilder.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    @SubCommand("leave")
    public void onBankJoinCommand(final @NotNull Player player,
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

        final UserBankData userBankData = optionalUserBankData.get();
        final String confirmRequest = leaveRequests.getIfPresent(player.getUniqueId());

        if (confirmRequest != null) {

            // Check if right confirmation
            if (!confirmRequest.equals(bankName)) {
                MessageUtil.sendMessage(player,
                        Language.BANK_LEFT_CONFIRM_WRONG,
                        MessageType.DENY,
                        "bank", bankName);
                return;
            }

            // Withdraw all the money in bank and delete account
            bankManager.withdraw(bank, player, userBankData.getBalance());
            bankManager.deleteAccount(bank, player);
            MessageUtil.sendMessage(player,
                    Language.BANK_LEFT,
                    MessageType.INFORMATION,
                    "bank", bankName);
        }

        // Send join request
        leaveRequests.put(player.getUniqueId(), bankName);
        MessageUtil.sendMessage(player,
                Language.BANK_JOIN_CONFIRM,
                MessageType.INFORMATION,
                "bank", bankName);

    }
}
