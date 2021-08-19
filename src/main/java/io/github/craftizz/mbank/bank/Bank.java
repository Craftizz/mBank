package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

public class Bank {

    private final Integer id;
    private final String name;

    private final Double minimumBalance;
    private final Double maximumBalance;

    public Bank(final @NotNull Integer id,
                final @NotNull String name,
                final @NotNull Double minimumBalance,
                final @NotNull Double maximumBalance) {

        this.id = id;
        this.name = name;
        this.minimumBalance = minimumBalance;
        this.maximumBalance = maximumBalance;
    }
}
