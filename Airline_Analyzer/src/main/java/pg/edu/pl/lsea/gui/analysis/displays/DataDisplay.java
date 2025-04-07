package pg.edu.pl.lsea.gui.analysis.displays;

import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.analysis.AnalysisArea;

import java.awt.Graphics;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

/**
 * Class responsible for displaying data when available
 */
public class DataDisplay extends AnalysisArea {
    final private List<Flight> flightData;
    final private List<Aircraft> aircraftData;
    /**
     * Add data to DataDisplay
     * @param flightData - the to be added flight data
     * @param aircraftData - the to be added aircraft data
     */
    public DataDisplay(List<Flight> flightData, List<Aircraft> aircraftData) {
        this.flightData = flightData;
        this.aircraftData = aircraftData;
    }

    /**
     * Generic method to display any data list - first few records
     * @param data - data to be displayed
     * @param g - the graphics
     * @param label - label to give the data
     * @param yPosition - position where the data should be situated
     */
    private <T> void showData(List<T> data, Graphics g, String label, int yPosition) {
        if (data != null && !data.isEmpty()) {
            g.drawString(label, 10, yPosition);
            for (int i = 0; i < Math.min(NUMBER_OF_RECORDS_SHOWN, data.size()); i++) {
                g.drawString(data.get(i).toString(), 10, yPosition + (i + 1) * 20);
            }
        }
    }


    /**
     * Displaying FlightData either/or AircraftData
     * @param g the Graphics object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Display Flight Data
        if (flightData != null && !flightData.isEmpty() && aircraftData != null && !aircraftData.isEmpty()) {
            showData(aircraftData, g, "Aircraft Data:", 40);
            showData(flightData, g, "Flight Data:", 80);
        }
    }
}
