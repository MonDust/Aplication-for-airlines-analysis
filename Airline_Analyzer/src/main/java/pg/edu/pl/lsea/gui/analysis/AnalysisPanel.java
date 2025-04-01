package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.analysis.displays.DataDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.DefaultDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class responsible for showing the analysis area
 */
public class AnalysisPanel extends JPanel {
    private DataStorage dataStorage;
    private JPanel currentDisplay;
    private int mode; // maybe?

    /**
     * Sets FlightData if available and updated the display
     * @param flights - list of flights to be added to storage.
     */
    public void setFlightData(List<Flight> flights) {
        for (Flight flight : flights) {
            dataStorage.addFlight(flight);
        }
    }

    /**
     * Sets the AircraftData if available and updates the display
     * @param aircrafts - list of aircrafts to be added to storage.
     */
    public void setAircraftData(List<Aircraft> aircrafts) {
        for (Aircraft aircraft : aircrafts) {
            dataStorage.addAircraft(aircraft);
        }
    }

    /**
     * Creates an AnalysisPanel object
     */
    public AnalysisPanel() {
        setLayout(null); // Can be later changed to other layout like FlowLayout
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        currentDisplay = new DefaultDisplay();
        add(currentDisplay);
    }

    /**
     * Fully remove the display from analysis panel.
     */
    public void removeDisplay() {
        if (currentDisplay != null) {
            remove(currentDisplay);
        }
        repaint();
    }

    /**
     * Show analysis of the data
     * @param g
     * @param label
     * @param yPosition
    private void showAnalysis(Graphics g, String label, int yPosition) {
        if (aircraftData != null && !aircraftData.isEmpty()) {
            nullRemover.TransformAircrafts(aircraftData);
        }

        if (flightData != null && !flightData.isEmpty()) {
            nullRemover.TransformFlights(flightData);
        }


        List<EnrichedFlight> enrichedFlights;
        enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flightData);

        sortingCaluclator.analyzeDataForDashbord(aircraftData, enrichedFlights);

    }
    */

    /**
     * Perform analysis on available data.
     * Analysis type (analysisType):
     * 1 - ...
     * 2 - ...
     * @param analysisType - type of the analysis to be performed
     */
    public void performAnalysis(int analysisType) {
        switch (analysisType) {
            case 1:
                //
                System.out.println("Analysis 1");
                break;
            case 2:
                //
                System.out.println("Analysis 2");
                break;
            default:
                //
        }
    }

}
