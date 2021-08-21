package io.github.craftizz.mbank.configuration;

import org.jetbrains.annotations.NotNull;

public enum Language {

    ALREADY_JOINED_BANK("already-joined-bank"),
    BANK_DEPOSITED("bank-deposited"),
    BANK_JOINED("bank-joined"),
    BANK_LEFT("bank-left"),
    MAXIMUM_BALANCE_REACHED("maximum-balance-reached"),
    NO_BANK_ACCESS("no-bank-access"),
    NO_PERMISSION_TO_JOIN("no-permission-to-join"),
    NONEXISTENT_BANK("nonexistent-bank"),
    NOT_ENOUGH_MINIMUM_BALANCE("not-enough-minimum-balance"),
    NOT_ENOUGH_MONEY("not-enough-money"),
    NOT_IN_BANK("not-in-bank");

    private final String configPath;
    private String message;

    Language(final @NotNull String configPath) {
        this.configPath = configPath;
    }

    /**
     * @return the configPath of the message
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * @return the message of the enum language
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message to {@param message}
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
