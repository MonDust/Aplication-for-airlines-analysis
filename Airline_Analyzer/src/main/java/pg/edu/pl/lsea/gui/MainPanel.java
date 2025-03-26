package pg.edu.pl.lsea.gui;

import pg.edu.pl.lsea.gui.analysis.AnalysisPanel;
import pg.edu.pl.lsea.gui.buttons.AnalyseDataButton;
import pg.edu.pl.lsea.gui.buttons.LoadAircraftDataButton;
import pg.edu.pl.lsea.gui.buttons.LoadDataButton;
import pg.edu.pl.lsea.gui.buttons.LoadFlightDataButton;

import javax.swing.*;
import java.awt.*;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_HEIGHT;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_WIDTH;

/**
 * Main Panel is the entire size of the Frame and contains other Panels
 */
public class MainPanel extends JPanel {
    private LoadDataButton loadFlightDataButton;
    private LoadDataButton loadAircraftDataButton;

    /**
     * Create a MainPanel object
     */
    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.white);

        loadFlightDataButton = new LoadFlightDataButton(this);
        loadAircraftDataButton = new LoadAircraftDataButton(this);
        add(loadFlightDataButton);
        add(loadAircraftDataButton);
    }

    /**
     * Method to set the current AnalysisPanel from the MainPanel
     * @param analysisPanel - the current AnalysisPanel
     */
    public void setAnalysisPanel(AnalysisPanel analysisPanel) {
        add(analysisPanel);
        loadAircraftDataButton.setAnalysisPanel(analysisPanel);
        loadFlightDataButton.setAnalysisPanel(analysisPanel);
    }
}
