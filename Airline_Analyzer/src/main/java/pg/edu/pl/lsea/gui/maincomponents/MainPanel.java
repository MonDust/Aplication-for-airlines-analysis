package pg.edu.pl.lsea.gui.maincomponents;

import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.analysis.AnalysisPanel;
import pg.edu.pl.lsea.gui.buttons.LoadAnalysisButton;
import pg.edu.pl.lsea.gui.buttons.LoadDataButton;
import pg.edu.pl.lsea.gui.buttons.LoadChoiceButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
    // Storage for data needed for analysis
    final private DataStorage dataStorage;

    /**
     * Create a MainPanel object
     */
    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.white);

        dataStorage = DataStorage.getInstance();

        analysisPanel = new AnalysisPanel();
        add(analysisPanel);

        mainLoadDataButton = new LoadChoiceButton(this);
        add(mainLoadDataButton);

        analyzeDataButton = new LoadAnalysisButton(this);
        add(analyzeDataButton);
    }

    /**
     * Get Flight data from data Storage
     * @return - the flight data
     */
    public List<Flight> getFlightData() {
        return dataStorage.getFlights();
    }

    /**
     * Get Aircraft data from data Storage
     * @return - the aircraft data
     */
    public List<Aircraft> getAircraftData() {
        return dataStorage.getAircrafts();
    }

    /**
     * The function that will call a specific function to perform analysis.
     * Analysis types (AnalysisType):
     * 1 - ...
     * 2 - ...
     * 3 - ...
     * @param analysisType - the type of analysis to be performed
     */
    public void performAnalysis(int analysisType) {
        analysisPanel.performAnalysis(analysisType);
    }


}
