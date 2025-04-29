package pg.edu.pl.lsea.gui.display;

import pg.edu.pl.lsea.api.DataLoader;

import javax.swing.*;

/**
 * Base analysis display class.
 */
public abstract class BaseAnalysisDisplay extends JPanel {
    protected DataLoader dataLoader = new DataLoader();
}
