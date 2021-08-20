package io.github.craftizz.mbank.configuration;

import org.jetbrains.annotations.NotNull;

public enum Language {

    ;

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
