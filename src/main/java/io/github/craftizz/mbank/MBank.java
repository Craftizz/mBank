package io.github.craftizz.mbank;

import io.github.craftizz.mbank.bank.Bank;
import io.github.craftizz.mbank.commands.*;
import io.github.craftizz.mbank.configuration.ConfigurationHandler;
import io.github.craftizz.mbank.configuration.Language;
import io.github.craftizz.mbank.database.DatabaseHandler;
import io.github.craftizz.mbank.hooks.CMIHook;
import io.github.craftizz.mbank.hooks.VaultHook;
import io.github.craftizz.mbank.listeners.PlayerJoinListener;
import io.github.craftizz.mbank.managers.BankManager;
import io.github.craftizz.mbank.managers.TaskManager;
import io.github.craftizz.mbank.managers.UserManager;
import io.github.craftizz.mbank.utils.MessageUtil;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.CompletionHandler;
import me.mattstudios.mf.base.MessageHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public final class MBank extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private ConfigurationHandler configurationHandler;

    private VaultHook vaultHook;
    private CMIHook cmiHook;

    private UserManager userManager;
    private BankManager bankManager;
    private TaskManager taskManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {


        // Initialize Database and Configuration
        this.databaseHandler = new DatabaseHandler(this);
        this.configurationHandler = new ConfigurationHandler(this);

        // Initialize Hooks
        this.vaultHook = new VaultHook();
        vaultHook.checkIfVaultExist(this);
        vaultHook.setupEconomy(this);
        vaultHook.setupPermission(this);

        this.cmiHook = new CMIHook();
        cmiHook.setupInstance(this);

        // Initialize Managers
        this.userManager = new UserManager(this);
        this.bankManager = new BankManager(this);
        this.taskManager = new TaskManager(this);
        this.commandManager = new CommandManager(this);

        // Initialize Listeners
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(this), this);

        // Initialize Commands
        registerTabCompletions();
        setCommandDefaultMessages();
        commandManager.register(
                new BankBalanceCommand(this),
                new BankDepositCommand(this),
                new BankJoinCommand(this),
                new BankLeaveCommand(this),
                new BankStatisticsCommand(this),
                new BankWithdrawCommand(this)
        );

        // Setup Configuration
        configurationHandler.setupLanguage();
        configurationHandler.loadBanks();

        userManager.startSaving();
        taskManager.startTask();

        new BankExpansion(this).register();
    }

    @Override
    public void onDisable() {
        userManager.saveAllUsers();
    }

    public void registerTabCompletions() {

        final CompletionHandler completionHandler = commandManager.getCompletionHandler();

        completionHandler.register("#banks", input -> bankManager
                .getBanks()
                .stream()
                .map(Bank::getId)
                .collect(Collectors.toList()));

        completionHandler.register("#amount", input -> List.of("1", "100", "1000", "10000", "100000"));
    }

    public void setCommandDefaultMessages() {
        final MessageHandler messageHandler = commandManager.getMessageHandler();
        messageHandler.register("cmd.no.exists", sender -> MessageUtil.sendMessage(sender, Language.COMMAND_NOT_EXIST));
        messageHandler.register("cmd.wrong.usage", sender -> MessageUtil.sendMessage(sender, Language.COMMAND_WRONG_USAGE));
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public BankManager getBankManager() {
        return bankManager;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }
}
