package io.github.craftizz.mbank.utils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class NumberUtils {

    private final static DecimalFormat formatter = new DecimalFormat("###,###,##0.00");

    public static String convertSeconds(final @NotNull Integer timeInSeconds) {

        final StringBuilder builder = new StringBuilder();

        int hours = timeInSeconds/ 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;

        if (hours > 0) {
            builder.append(hours)
                    .append("h ");
        }

        if (minutes > 0) {
            builder.append(minutes)
                    .append("m ");
        }

        if (seconds > 0) {
            builder.append(seconds)
                    .append("s");
        }

        return builder.toString();
    }

    public static String formatCurrency(final @NotNull Double amount) {
        return formatter.format(amount);
    }

}
