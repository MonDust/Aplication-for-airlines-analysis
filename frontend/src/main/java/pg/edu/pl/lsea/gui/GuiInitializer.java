package pg.edu.pl.lsea.gui;

import pg.edu.pl.lsea.gui.maincomponents.MainFrame;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

/**
 * Class initializing whole GUI of the application for analysing the data
 */
public class GuiInitializer {
    /**
     * Function initializing the GUI - main panel and then main frame
     */
    public static void initialize() {
        MainPanel mainPanel = new MainPanel();

        MainFrame mainFrame = new MainFrame(mainPanel);
    }
}