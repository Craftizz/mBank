package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

public class Bank {

    private final String id;
    private final String displayName;

    private final Restrictions restrictions;
    private final Interest interest;
    private final Commands commands;
    private final Fees fees;
    private final Crisis crisis;

    public Bank(final @NotNull String id,
                final @NotNull String displayName,
                final @NotNull Restrictions restrictions,
                final @NotNull Interest interest,
                final @NotNull Commands commands,
                final @NotNull Fees fees,
                final @NotNull Crisis crisis) {

        this.id = id;
        this.displayName = displayName;
        this.restrictions = restrictions;
        this.interest = interest;
        this.commands = commands;
        this.fees = fees;
        this.crisis = crisis;
    }

    /**
     * @return the id of this bank
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name of this bank
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the {@link Restrictions} of this bank
     */
    public Restrictions getLimits() {
        return restrictions;
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

    /**
     * @return the {@link Restrictions} of this bank
     */
    public Restrictions getRestrictions() {
        return restrictions;
    }

    /**
     * @return the {@link Fees} of this bank
     */
    public Fees getFees() {
        return fees;
    }

    /**
     * @return the {@link Crisis} of this bank
     */
    public Crisis getCrisis() {
        return crisis;
    }
}
