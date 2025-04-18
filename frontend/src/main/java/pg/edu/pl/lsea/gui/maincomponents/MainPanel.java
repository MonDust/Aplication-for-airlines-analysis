package pg.edu.pl.lsea.gui.maincomponents;

import pg.edu.pl.lsea.gui.maincomponents.AnalysisPanel;
import pg.edu.pl.lsea.gui.buttons.LoadAnalysisButton;
import pg.edu.pl.lsea.gui.buttons.LoadDataButton;
import pg.edu.pl.lsea.gui.buttons.LoadChoiceButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_HEIGHT;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_WIDTH;

/**
 * Main Panel is the entire size of the Frame and contains other Panels
 */
public class MainPanel extends JPanel {
    // Data Button for loading the data - after which the window of choices will be shown
    final private LoadDataButton mainLoadDataButton;
    // Data Button for analysing it - after which the choices of analysis will be shown
    final private LoadDataButton analyzeDataButton;
    // Panel where analysis of the data is shown
    final private AnalysisPanel analysisPanel;
    // Last directory that file was gotten from.
    private File lastDirectory;

    /**
     * Create a MainPanel object
     */
    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.white);

        analysisPanel = new AnalysisPanel();
        add(analysisPanel);

        mainLoadDataButton = new LoadChoiceButton(this);
        add(mainLoadDataButton);

        analyzeDataButton = new LoadAnalysisButton(this);
        add(analyzeDataButton);
    }

    /**
     * Sets the last directory from which files were loaded.
     * @param directory The directory to set as last used.
     */
    public void setLastDirectory(File directory) {
        this.lastDirectory = directory;
    }

    /**
     * Gets the last directory from which files were loaded.
     * @return The last directory used, or null if not set.
     */
    public File getLastDirectory() {
        return lastDirectory;
    }

    public void showAnalysis(int analysisType) {
        analysisPanel.displayAnalysis(analysisType);
    }


}
