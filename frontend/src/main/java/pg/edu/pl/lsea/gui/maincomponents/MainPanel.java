package pg.edu.pl.lsea.gui.maincomponents;

import pg.edu.pl.lsea.gui.buttons.*;

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
    final private BaseDataButton mainLoadDataButton;
    // Data Button for analysing it - after which the choices of analysis will be shown
    final private BaseDataButton analyzeDataButton;
    // Data Button for showing information about records already loaded
    final private BaseDataButton showInformationButton;
    // Data Button for updating/deleting information about records already loaded
    final private BaseDataButton updateDataButton;
    // Panel where analysis of the data is shown
    final private DisplayPanel displayPanel;
    // Last directory that file was gotten from.
    private File lastDirectory;

    /**
     * Create a MainPanel object
     */
    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.white);

        displayPanel = new DisplayPanel();
        add(displayPanel);

        mainLoadDataButton = new LoadChoiceButton(this);
        add(mainLoadDataButton);

        analyzeDataButton = new LoadAnalysisButton(this);
        add(analyzeDataButton);

        showInformationButton = new InformationButton(this);
        add(showInformationButton);

        updateDataButton = new UpdateButton(this);
        add(updateDataButton);
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
        displayPanel.displayAnalysis(analysisType);
    }

    public void showInformation(int informationType) { displayPanel.showInformation(informationType); }

    public void makeUpdate(int selectedType) { displayPanel.makeUpdate(selectedType);}


}
