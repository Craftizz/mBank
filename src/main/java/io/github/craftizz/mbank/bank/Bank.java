package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

public class Bank {

    private final Integer id;
    private final String name;

    private final Limits limits;
    private final Interest interest;


    public Bank(final @NotNull Integer id,
                final @NotNull String name,
                final @NotNull Limits limits,
                final @NotNull Interest interest) {

        this.id = id;
        this.name = name;
        this.limits = limits;
        this.interest = interest;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Limits getLimits() {
        return limits;
    }

    public Interest getInterest() {
        return interest;
    }
}
