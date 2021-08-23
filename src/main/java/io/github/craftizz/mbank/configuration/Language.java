package io.github.craftizz.mbank.configuration;

import org.jetbrains.annotations.NotNull;

public enum Language {

    ALREADY_JOINED_BANK("already-joined-bank"),
    BANK_BALANCE("bank-balance"),
    BANK_BALANCE_SPECIFIC("bank-balance-specific"),
    BANK_CRISIS_LOST("bank-crisis-lost"),
    BANK_CRISIS_NOT_AFFECTED("bank-crisis-not-affected"),
    BANK_DEPOSITED("bank-deposited"),
    BANK_INTEREST_EARN("bank-interest-earn"),
    BANK_JOINED("bank-joined"),
    BANK_JOIN_CONFIRM("bank-join-confirm"),
    BANK_JOIN_CONFIRM_WRONG("bank-join-confirm-wrong"),
    BANK_LEFT("bank-left"),
    BANK_LEFT_CONFIRM("bank-left-confirm"),
    BANK_LEFT_CONFIRM_WRONG("bank-left-confirm-wrong"),
    BANK_WITHDRAWN("bank-withdrawn"),
    COMMAND_NOT_EXIST("command-not-exist"),
    COMMAND_WRONG_USAGE("command-wrong-usage"),
    GUI_TITLE("gui-title"),
    INTEREST_MAX_BALANCE("interest-max-balance"),
    DO_NOT_HAVE_BANK("do-not-have-bank"),
    MAXIMUM_BALANCE_REACHED("maximum-balance-reached"),
    MAXIMUM_BANK_REACHED("maximum-bank-reached"),
    NEGATIVE_NOT_ALLOWED("negative-not-allowed"),
    NO_BANK_ACCESS("no-bank-access"),
    NO_PERMISSION_TO_JOIN("no-permission-to-join"),
    NONEXISTENT_BANK("nonexistent-bank"),
    NOT_ENOUGH_MINIMUM_BALANCE("not-enough-minimum-balance"),
    NOT_ENOUGH_MONEY("not-enough-money"),
    NOT_ENOUGH_MONEY_IN_BANK("not-enough-money-in-bank"),
    NOT_IN_BANK("not-in-bank"),
    ONLY_INTEGERS("only-integers"),
    WITHDRAW_DENY_TIME("withdraw-deny-time");

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
