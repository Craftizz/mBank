package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

public class Bank {

    private final Integer id;
    private final String name;

    private final Limits limits;
    private final Interest interest;
    private final Commands commands;


    public Bank(final @NotNull Integer id,
                final @NotNull String name,
                final @NotNull Limits limits,
                final @NotNull Interest interest,
                final @NotNull Commands commands) {

        this.id = id;
        this.name = name;
        this.limits = limits;
        this.interest = interest;
        this.commands = commands;
    }

    /**
     * @return the id of this bank
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the name of this bank
     */
    public String getName() {
        return name;
    }

    /**
     * @return the {@link Limits} of this bank
     */
    public Limits getLimits() {
        return limits;
    }

    /**
     * @return the {@link Interest} of this bank
     */
    public Interest getInterest() {
        return interest;
    }

    /**
     * @return the {@link Commands} of this bank
     */
    public Commands getCommands() {
        return commands;
    }
}
