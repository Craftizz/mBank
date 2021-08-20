package io.github.craftizz.mbank.bank;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Restrictions {

    private final Double minimumBalance;
    private final Double maximumBalance;
    private final Integer withdrawInterval;
    private final List<String> permissionRequirements;

    public Restrictions(final @NotNull Double minimumBalance,
                        final @NotNull Double maximumBalance,
                        final @NotNull Integer withdrawInterval,
                        final @NotNull List<String> permissionRequirements) {

        this.minimumBalance = minimumBalance;
        this.maximumBalance = maximumBalance;
        this.withdrawInterval = withdrawInterval;
        this.permissionRequirements = permissionRequirements;
    }

    /**
     * @return the minimum balance to open on the bank
     */
    public Double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * @return the maximum balance to open on the bank
     */
    public Double getMaximumBalance() {
        return maximumBalance;
    }

    /**
     * @return the withdrawal interval limit in seconds
     */
    public Integer getWithdrawInterval() {
        return withdrawInterval;
    }

    /**
     * @return the permission requirements of the bank
     */
    public List<String> getPermissionRequirements() {
        return permissionRequirements;
    }
}
