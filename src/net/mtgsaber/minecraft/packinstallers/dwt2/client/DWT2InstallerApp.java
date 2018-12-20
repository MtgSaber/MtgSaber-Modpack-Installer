package net.mtgsaber.minecraft.packinstallers.dwt2.client;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.mtgsaber.lib.Clock;
import net.mtgsaber.lib.TaskStack;
import net.mtgsaber.lib.mtgsaberfxlib.StdApp;
import net.mtgsaber.minecraft.packinstallers.dwt2.Process;

/**
 * Author: Andrew Arnold (10/1/2018)
 */
public class DWT2InstallerApp extends StdApp {
    private DWT2InstallerPane pane;
    private DWT2InstallerPane_Events pane_events;
    private Clock clkPrimary;
    private Thread clkThreadPrimary;
    private TaskStack taskStackPrimary;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // init
        pane = new DWT2InstallerPane(this);
        pane_events = new DWT2InstallerPane_Events(this, pane);
        clkPrimary = new Clock(500);
        clkThreadPrimary = new Thread(clkPrimary);
        taskStackPrimary = new TaskStack();

        // setup
        pane_events.hookStage(primaryStage);
        pane_events.hookEvents();
        primaryStage.setOnCloseRequest(event -> clkPrimary.isRunning = false);

        // start
        clkPrimary.isRunning = true;
        clkThreadPrimary.start();
        primaryStage.show();
    }
}
