package net.mtgsaber.minecraft.packinstallers.dwt2.client;

import net.mtgsaber.lib.mtgsaberfxlib.standardgui.StdPane;
import net.mtgsaber.minecraft.packinstallers.dwt2.client.DWT2InstallerApp;

/**
 * Author: Andrew Arnold (10/1/2018)
 */
public class DWT2InstallerPane extends StdPane {
    private final DWT2InstallerApp APP;

    public DWT2InstallerPane(DWT2InstallerApp app) {
        super("DWT2InstallerPane", true, true);
        this.APP = app;
    }

    @Override
    public void lockControls() {
        super.lockControls();
    }

    @Override
    public void unlockControls() {
        super.unlockControls();
    }
}
