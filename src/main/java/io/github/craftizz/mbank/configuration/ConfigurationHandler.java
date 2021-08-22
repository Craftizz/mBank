package io.github.craftizz.mbank.configuration;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.*;
import io.github.craftizz.mbank.managers.BankManager;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigurationHandler {

    private final MBank plugin;

    private final Yaml bankConfig;
    private final Yaml language;
    private final Yaml guiYaml;
    private final Yaml config;
    private final Yaml data;

    public ConfigurationHandler(final @NotNull MBank plugin) {

        this.plugin = plugin;

        bankConfig = LightningBuilder
                .fromPath("bank", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("bank.yml")
                .createYaml();

        language = LightningBuilder
                .fromPath("lang", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("lang.yml")
                .createYaml();

        guiYaml = LightningBuilder
                .fromPath("gui", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("gui.yml")
                .createYaml();

        config = LightningBuilder
                .fromPath("config", plugin.getDataFolder().getAbsolutePath())
                .addInputStreamFromResource("config.yml")
                .createYaml();

        data = LightningBuilder
                .fromPath("data", plugin.getDataFolder().getAbsolutePath() + "/data")
                .createYaml();
    }

    public void setupLanguage() {
        for (Language languageEnum : Language.values()) {
            languageEnum.setMessage(language.getString(languageEnum.getConfigPath()));
        }
    }

    public void setupConfig() {

        Config.setSaveTime(config.getLong("save-time") * 20);
        MessageType.INFORMATION.setSound(config.getEnum("info-sound", Sound.class));
        MessageType.DENY.setSound(config.getEnum("deny-sound", Sound.class));

    }

    public void loadBanks() {

        final BankManager bankManager = plugin.getBankManager();

        for (String bankKey : bankConfig.singleLayerKeySet()) {

            final FlatFileSection section = bankConfig.getSection(bankKey);

            // General Information
            final String bankId = section.getString("id");
            final String bankName = section.getString("display-name");

            // Restriction
            final Double minimumBalance = section.getDouble("restrictions" + ".minimum-balance");
            final Double maximumBalance = section.getDouble("restrictions" + ".maximum-balance");
            final Integer withdrawInterval = section.getInt("restrictions" + ".withdraw-interval");
            final List<String> permissions = section.getStringList("restriction" + ".permissions");

            // Interest
            final Double interest = section.getDouble("interest" + ".value");
            final Integer interestInterval = section.getInt("interest" + ".interval");

            // Commands
            final List<String> joinCommands = section.getStringList("commands" + ".join-commands");
            final List<String> leaveCommands = section.getStringList("commands" + ".leave-commands");

            // Fees
            final Double depositFee = section.getDouble("fees" + ".deposit-fee");
            final Double withdrawFee = section.getDouble("fees" + ".withdraw-fee");

            // Crisis
            final Double chanceToHappen = section.getDouble("crisis" + ".chance-to-happen");
            final Double chanceToLose = section.getDouble("crisis" + ".chance-to-lose");
            final Double minimumLostInPercentage = section.getDouble("crisis" + ".minimum-lost");
            final Double maximumLostInPercentage = section.getDouble("crisis" + ".maximum-lost");
            final Integer crisisInterval = section.getInt("crisis" + ".interval");

            final Bank bank = new Bank(bankId, bankName,
                    new Restrictions(minimumBalance, maximumBalance, withdrawInterval, permissions),
                    new Interest(interest, interestInterval),
                    new Commands(joinCommands, leaveCommands),
                    new Fees(depositFee, withdrawFee),
                    new Crisis(chanceToHappen, chanceToLose, minimumLostInPercentage, maximumLostInPercentage, crisisInterval));

            bankManager.addBank(bank);

            plugin.getLogger().warning("Loaded " + bank.getId() + " bank");
        }
    }

    public Yaml getConfig() {
        return config;
    }

    public Yaml getGuiYaml() {
        return guiYaml;
    }

    public Yaml getBankConfig() {
        return bankConfig;
    }

    public Yaml getLanguage() {
        return language;
    }

    public Yaml getData() {
        return data;
    }
}
