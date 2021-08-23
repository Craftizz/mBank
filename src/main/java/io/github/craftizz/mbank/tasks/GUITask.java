package io.github.craftizz.mbank.tasks;

import dev.triumphteam.gui.guis.Gui;
import io.github.craftizz.mbank.gui.BankStatisticsGUI;
import io.github.craftizz.mbank.tasks.tasktypes.BooleanTask;
import org.jetbrains.annotations.NotNull;

public class GUITask extends BooleanTask {

    private final BankStatisticsGUI bankStatisticsGUI;

    public GUITask(final @NotNull BankStatisticsGUI bankStatisticsGUI) {
        this.bankStatisticsGUI = bankStatisticsGUI;

        final Gui gui = bankStatisticsGUI.getGui();
        gui.setCloseGuiAction(event -> setShouldReschedule(false));
    }

    @Override
    public void compute() {

        final Gui gui = bankStatisticsGUI.getGui();
        bankStatisticsGUI.update();
    }
}
