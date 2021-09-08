package io.github.craftizz.mbank.tasks;

import dev.triumphteam.gui.guis.Gui;
import io.github.craftizz.mbank.gui.BankStatisticsGUI;
import io.github.craftizz.mbank.tasks.tasktypes.TimedTask;
import org.jetbrains.annotations.NotNull;

public class GUITask extends TimedTask {

    private final BankStatisticsGUI bankStatisticsGUI;

    private boolean shouldReschedule = true;

    public GUITask(final @NotNull BankStatisticsGUI bankStatisticsGUI) {
        super(20);
        this.bankStatisticsGUI = bankStatisticsGUI;

        final Gui gui = bankStatisticsGUI.getGui();
        gui.setCloseGuiAction(event -> this.shouldReschedule = false);
    }

    @Override
    public void compute() {
        bankStatisticsGUI.update();
    }

    @Override
    public boolean shouldReschedule() {
        return shouldReschedule;
    }
}
