package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class responsible for showing the analysis area
 */
public class AnalysisPanel extends JPanel {
    private java.util.List<Flight> flightData;
    private List<Aircraft> aircraftData;
    private JPanel currentDisplay;

    /**
     * Sets FlightData if available and updated the display
     * @param flights
     */
    public void setFlightData(List<Flight> flights) {
        this.flightData = flights;
        updateDisplay();
        repaint();
    }

    /**
     * Sets the AircraftData if available and updates the display
     * @param aircrafts
     */
    public void setAircraftData(List<Aircraft> aircrafts) {
        this.aircraftData = aircrafts;
        updateDisplay();
        repaint();
    }

    /**
     * Creates an AnalysisPanel object
     */
    public AnalysisPanel() {
        setLayout(null); // Can be later changed to other layout like FlowLayout
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        updateDisplay();
    }

    /**
     * Updated the current display, so it shows the available data
     */
    private void updateDisplay() {
        System.out.println("HELLO");

        // Remove any previous component
        if (currentDisplay != null) {
            remove(currentDisplay);
        }

        // Determine whether to show the chart or data
        if ((flightData == null || flightData.isEmpty()) && (aircraftData == null || aircraftData.isEmpty())) {
            // No data, show the chart
            currentDisplay = new Chart();
        } else {
            // Data available, show the data display
            currentDisplay = new DataDisplay(flightData, aircraftData);
        }

        // Add the appropriate component
        add(currentDisplay);
    }

}
