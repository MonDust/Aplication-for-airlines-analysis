package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisArea;
import pg.edu.pl.lsea.gui.analysis.displays.datadisplays.ThreadAnalysisRunner;
import pg.edu.pl.lsea.gui.analysis.displays.datadisplays.OutputTableDisplay;
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
    private AnalysisArea currentDisplay;
    NullRemover nullRemover = new NullRemover();
    DataEnrichment enricher = new DataEnrichment();

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

        dataStorage = DataStorage.getInstance();
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

    public List<EnrichedFlight> prepareFlights() {
        List<Flight> flights = dataStorage.getFlights();
        nullRemover.TransformFlights(flights);
        List<EnrichedFlight> enrichedFlights = enricher.CreateEnrichedListOfFlights(flights);
        return enrichedFlights;
    }

    public List<Aircraft> prepareAircrafts() {
        List<Aircraft> aircrafts = dataStorage.getAircrafts();
        nullRemover.TransformAircrafts(aircrafts);
        return aircrafts;
    }

    private int promptForThreadCount() {
        Integer[] options = {1, 2, 4, 8, 16};
        Integer selection = (Integer) JOptionPane.showInputDialog(
                null,
                "Select number of threads for parallel analysis:",
                "Thread Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                4 // default
        );

        return selection != null ? selection : 4; // default to 4 if user cancels
    }


    /**
     * Perform analysis on available data.
     * Analysis type (analysisType):
     * 1 - ...
     * 2 - ...
     * @param analysisType - type of the analysis to be performed
     */
    public void performAnalysis(int analysisType) {
        removeDisplay();
        List<EnrichedFlight> enrichedFlights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();
        SortingCalculator calc = new SortingCalculator();

        switch (analysisType) {
            case 1:
                System.out.println("Comparing sequential and parallel performance...");
                ThreadAnalysisRunner runner = new ThreadAnalysisRunner();

                int threads = promptForThreadCount();
                runner.runAnalysis(false, 1, enrichedFlights, aircrafts);  // Single-threaded
                runner.runAnalysis(true, threads, enrichedFlights, aircrafts);   // Multi-threaded

                JOptionPane.showMessageDialog(null, runner.getMessages(), "Analysis Report", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                System.out.println("Sorting by the amount of flights...");
                List<Output> sortedByCount = calc.sortByAmountOfFlights(enrichedFlights);
                currentDisplay = new OutputTableDisplay(sortedByCount);
                currentDisplay.showAsPopup("amount of flights");
                break;
            case 3:
                System.out.println("Sorting by the time of flights...");
                List<Output> sortedByTime = calc.sortByTimeOfFlights(enrichedFlights);
                currentDisplay = new OutputTableDisplay(sortedByTime);
                currentDisplay.showAsPopup( "time of flights");
                break;
            default:
                add(currentDisplay);
                currentDisplay = new DefaultDisplay();
        }
        repaint();
    }

}
