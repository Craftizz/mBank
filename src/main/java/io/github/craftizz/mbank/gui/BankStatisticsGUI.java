package io.github.craftizz.mbank.gui;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.sections.FlatFileSection;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.craftizz.mbank.bank.*;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.bank.user.UserBankData;
import io.github.craftizz.mbank.utils.MessageUtil;
import io.github.craftizz.mbank.utils.NumberUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class BankStatisticsGUI {

    private final Gui gui;

    public BankStatisticsGUI(final @NotNull UserBankData bankData,
                             final @NotNull Bank bank,
                             final @NotNull Yaml guiYaml) {

        final Component title = MessageUtil.parse(guiYaml.getString("statistics.title"), "bank", bank.getId());
        final Material filler = Material.getMaterial(guiYaml.getString("statistics.filler"));

        this.gui = Gui.gui()
                .title(title)
                .rows(5)
                .create();
        gui.getFiller().fill(ItemBuilder.from(filler).asGuiItem());

        setup(bankData, bank, guiYaml);
    }

    private void setup(final @NotNull UserBankData bankData,
                       final @NotNull Bank bank,
                       final @NotNull Yaml guiYaml) {

        final Double balance = bankData.getBalance();
        final Interest interest = bank.getInterest();
        final Restrictions restrictions = bank.getRestrictions();
        final Fees fees = bank.getFees();
        final Crisis crisis = bank.getCrisis();

        // Balance Section
        final FlatFileSection balanceSection = guiYaml.getSection("statistics.balance");
        final Material balanceMaterial = balanceSection.getEnum("material", Material.class);
        if (balanceMaterial != null) {
            final GuiItem balanceItem = ItemBuilder.from(balanceMaterial)
                    .name(MessageUtil.parse(balanceSection.getString("title")))
                    .lore(MessageUtil.parseMany(balanceSection.getStringList("lore"),
                            "amount", NumberUtils.formatCurrency(balance),
                            "time", NumberUtils.convertSeconds(LocalDateTime.now().until(bankData.getLastWithdraw().plusSeconds(bank.getRestrictions().getWithdrawInterval()), ChronoUnit.SECONDS)),
                            "maxamount", NumberUtils.formatCurrency(restrictions.getMaximumBalance()),
                            "deposit", String.valueOf(fees.getDepositFee() * 100),
                            "withdraw", String.valueOf(fees.getWithdrawFee() * 100)))
                    .asGuiItem();
            gui.setItem(balanceSection.getInt("slot"), balanceItem);
        }

        // Interest Section
        final FlatFileSection interestSection = guiYaml.getSection("statistics.interest");
        final Material interestMaterial = interestSection.getEnum("material", Material.class);

        final double earning1 = balance + interest.calculateInterest(balance);
        final double earning2 = earning1 + (interest.calculateInterest(earning1));
        final double earning3 = earning2 + (interest.calculateInterest(earning2));
        final double earning4 = earning3 + (interest.calculateInterest(earning3));

        if (interestMaterial != null) {
            final GuiItem balanceItem = ItemBuilder.from(interestMaterial)
                    .name(MessageUtil.parse(interestSection.getString("title")))
                    .lore(MessageUtil.parseMany(interestSection.getStringList("lore") ,
                            "time1", NumberUtils.convertSeconds(interest.getNextPayout()),
                            "earning1", NumberUtils.formatCurrency(earning1),
                            "time2", NumberUtils.convertSeconds(interest.getNextPayout() + interest.getPayoutInterval()),
                            "earning2", NumberUtils.formatCurrency(earning2),
                            "time3", NumberUtils.convertSeconds(interest.getNextPayout() + (interest.getPayoutInterval() * 2)),
                            "earning3", NumberUtils.formatCurrency(earning3),
                            "time4", NumberUtils.convertSeconds(interest.getNextPayout() + (interest.getPayoutInterval() * 3)),
                            "earning4", NumberUtils.formatCurrency(earning4)))
                    .asGuiItem();
            gui.setItem(interestSection.getInt("slot"), balanceItem);
        }

        // Crisis
        final FlatFileSection crisisSection = guiYaml.getSection("statistics.crisis");
        final Material crisisMaterial = crisisSection.getEnum("material", Material.class);

        if (crisisMaterial != null) {
            final GuiItem balanceItem = ItemBuilder.from(crisisMaterial)
                    .name(MessageUtil.parse(crisisSection.getString("title")))
                    .lore(MessageUtil.parseMany(crisisSection.getStringList("lore") ,
                            "time", NumberUtils.convertSeconds(crisis.getNextCrisis()),
                            "minimum", NumberUtils.formatCurrency(balance * crisis.getMinimumLostInPercentage()),
                            "maximum", NumberUtils.formatCurrency(balance * crisis.getMaximumLostInPercentage()),
                            "amount", NumberUtils.formatCurrency(bankData.getLostInLastCrisis()),
                            "chancehappen", String.valueOf(crisis.getChanceToHappen() * 100),
                            "chanceaffected", String.valueOf(crisis.getChanceToLose() * 100)
                            ))
                    .asGuiItem();
            gui.setItem(crisisSection.getInt("slot"), balanceItem);
        }
    }

    public void open(final @NotNull Player player) {
        gui.open(player);
    }


}
