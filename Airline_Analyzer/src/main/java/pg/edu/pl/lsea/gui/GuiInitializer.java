package pg.edu.pl.lsea.gui;

import pg.edu.pl.lsea.gui.maincomponents.MainFrame;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

public class GuiInitializer {
    public static void initialize() {
        MainPanel mainPanel = new MainPanel();

        MainFrame mainFrame = new MainFrame(mainPanel);
    }
}

// TO DO:
// - analysis button
// - adding flights to the list, not just changing it -> using the data storage (not just lists)
