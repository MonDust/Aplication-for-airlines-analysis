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
    private LoadDataButton mainLoadDataButton;
    // Data Button for analysing it - after which the choices of analysis will be shown
    private LoadDataButton analyzeDataButton;
    // Panel where analysis of the data is shown
    private AnalysisPanel analysisPanel;
    // Storage for data needed for analysis
    private DataStorage dataStorage;

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
     * Set Flight data
     * @param flights - list of aircrafts to be added to data storage
     */
    public void setFlightData(List<Flight> flights) {
        for (Flight flight : flights) {
            dataStorage.addFlight(flight);
        }
    }

    /**
     * Set Aircraft data
     * @param aircrafts - list of aircrafts to be added to data storage
     */
    public void setAircraftData(List<Aircraft> aircrafts) {
        for (Aircraft aircraft : aircrafts) {
            dataStorage.addAircraft(aircraft);
        }
    }

    /**
     * Make data ready for analysis
     */
    public void finalizeDataForAnalysis() {
        dataStorage.finalizeData();
    }

    /**
     * Change data back so adding new data is possible
     */
    public void revertFinalizationOfData() {
        dataStorage.revertFinalization();
    }

    /**
     * Get Flight data from data Storage
     * @return - the flight data
     */
    public List<Flight> getFlightData() {
        if(dataStorage.isFinalized()) {
            return dataStorage.getFlights();
        }
        else throw new IllegalStateException("Data not finalized!");
    }

    /**
     * Get Aircraft data from data Storage
     * @return - the aircraft data
     */
    public List<Aircraft> getAircraftData() {
        if(dataStorage.isFinalized()) {
            return dataStorage.getAircrafts();
        }
        else throw new IllegalStateException("Data not finalized!");
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
        if(dataStorage.isFinalized()) {
            analysisPanel.performAnalysis(analysisType);
        }
        else {
            throw new IllegalStateException("Data not finalized!");
        }
    }


}
