package net.mtgsaber.minecraft.packinstallers.dwt2.client;

import javafx.stage.Stage;
import net.mtgsaber.lib.mtgsaberfxlib.standardgui.StdPaneEvents;
import net.mtgsaber.minecraft.packinstallers.dwt2.client.DWT2InstallerApp;
import net.mtgsaber.minecraft.packinstallers.dwt2.client.DWT2InstallerPane;

/**
 * Author: Andrew Arnold (10/1/2018)
 */
public class DWT2InstallerPane_Events extends StdPaneEvents {
    private final DWT2InstallerApp APP;
    private final DWT2InstallerPane PANE;

    public DWT2InstallerPane_Events(DWT2InstallerApp app, DWT2InstallerPane pane) {
        super(pane, app);
        this.APP = app;
        this.PANE = pane;
    }

    @Override
    public void hookEvents() {
        super.hookEvents();
    }

    @Override
    public void unhookEvents() {
        super.unhookEvents();
    }

    @Override
    public void hookStage(Stage stage) {
        super.hookStage(stage);
    }

    @Override
    public void unhookStage(Stage stage) {
        super.unhookStage(stage);
    }
}
