package io.github.craftizz.mbank.database;

import de.leonhard.storage.Yaml;
import io.github.craftizz.mbank.MBank;
import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.bank.Crisis;
import io.github.craftizz.mbank.bank.Interest;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class PersistenceHandler {

    private final MBank plugin;

    public PersistenceHandler(final @NotNull MBank plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads the persistent values from data.yml to
     * be set on the bank values. Used onEnable();
     */
    public void loadPersistent() {

        final Logger logger = plugin.getLogger();
        final Yaml config = plugin.getConfigurationHandler().getData();

        for (final Bank bank : plugin.getBankManager().getBanks()) {

            // Set Interest Time
            final Interest interest = bank.getInterest();
            interest.setTimeLeft(config.getOrSetDefault(bank.getId() + ".interest-time-left", interest.getPayoutInterval()));
            logger.warning(bank.getId() + " - interest time left has been set to " + interest.getNextPayout());

            // Set Crisis Time
            final Crisis crisis = bank.getCrisis();
            crisis.setTimeLeft(config.getOrSetDefault(bank.getId() + ".crisis-time-left", crisis.getNextRandomInterval()));
            logger.warning(bank.getId() + " - crisis time left has been set to " + crisis.getNextCrisis());

        }
    }

    /**
     * Saves the persistent values to data.yml to be
     * loaded in any time of restart
     */
    public void savePersistent() {

        final Yaml config = plugin.getConfigurationHandler().getData();

        for (final Bank bank : plugin.getBankManager().getBanks()) {

            // Set Interest Time
            final Interest interest = bank.getInterest();
            config.set(bank.getId() + ".interest-time-left", interest.getNextPayout());

            // Set Crisis Time
            final Crisis crisis = bank.getCrisis();
            config.set(bank.getId() + ".crisis-time-left", crisis.getNextCrisis());
        }
    }
}
