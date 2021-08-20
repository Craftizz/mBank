package io.github.craftizz.mbank.configuration;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.*;
import io.github.craftizz.mbank.managers.BankManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigurationHandler {

    private final MBank plugin;

    private final Yaml bankConfig;
    private final Yaml language;

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
    }

    public void setupLanguage() {
        for (Language languageEnum : Language.values()) {
            languageEnum.setMessage(language.getString(languageEnum.getConfigPath()));
        }
    }

    public void loadBanks() {

        final BankManager bankManager = plugin.getBankManager();

        for (String bankKey : bankConfig.singleLayerKeySet()) {

            final FlatFileSection section = bankConfig.getSection(bankKey);

            // General Information
            final String bankId = section.getString("id");
            final String bankName = section.getString("display-name");

            // Restriction
            final Double minimumBalance = section.getDouble("restriction" + ".minimum-balance");
            final Double maximumBalance = section.getDouble("restriction" + ".maximum-balance");
            final Integer withdrawInterval = section.getInt("restriction" + ".withdraw-interval");
            final List<String> permissions = section.getStringList("restriction" + ".permissions");

            // Interest
            final Double interest = section.getDouble("interest" + ".value");
            final Integer interval = section.getInt("interest" + ".interval");

            // Commands
            final List<String> joinCommands = section.getStringList("commands" + ".join-commands");
            final List<String> leaveCommands = section.getStringList("commands" + ".leave-commands");

            // Fees
            final Double depositFee = section.getDouble("fees" + ".deposit-fee");
            final Double withdrawFee = section.getDouble("fees" + ".withdraw-fee");

            final Bank bank = new Bank(bankId, bankName,
                    new Restrictions(minimumBalance, maximumBalance, withdrawInterval, permissions),
                    new Interest(interest, interval),
                    new Commands(joinCommands, leaveCommands),
                    new Fees(depositFee, withdrawFee));

            bankManager.addBank(bank);

            plugin.getLogger().warning("Loaded " + bank.getId() + " bank");
        }

    }

}
