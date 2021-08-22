package io.github.craftizz.mbank.gui;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.sections.FlatFileSection;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.craftizz.mbank.bank.user.User;
import io.github.craftizz.mbank.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;



public class BankStatisticsGUI {

    private final Gui gui;

    public BankStatisticsGUI(final @NotNull User user,
                             final @NotNull Yaml guiYaml) {

        final Component title = MessageUtil.parse(guiYaml.getString("statistics.title"));
        final Material filler = Material.getMaterial(guiYaml.getString("statistics.filler"));

        this.gui = Gui.gui()
                .title(title)
                .rows(6)
                .create();
        gui.getFiller().fill(ItemBuilder.from(filler).asGuiItem());

        setup(user, guiYaml);
    }

    private void setup(final @NotNull User user,
                       final @NotNull Yaml guiYaml) {

        // Balance

        final FlatFileSection balanceSection = guiYaml.getSection("statistics.balance");
        final Component balanceTitle = MessageUtil.parse(balanceSection.getString("title"));
        final Component balanceLore = MessageUtil.parse(balanceSection.getString("lore"));
        final Material balanceMaterial = Material.getMaterial(balanceSection.getString("material"));
        final int balanceSlot = balanceSection.getInt("slot");


        final GuiItem balanceItem = ItemBuilder.from(balanceMaterial)
                .name(balanceTitle)
                .lore(balanceLore)
                .asGuiItem();
        gui.setItem(balanceSlot, balanceItem);


        // Interest


        // Crisis


        // Fees

    }

    public void open(final @NotNull Player player) {
        gui.open(player);
    }


}
