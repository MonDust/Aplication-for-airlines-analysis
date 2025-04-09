package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.datadisplays.PlotAverageTimePerOperatorDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.results.ThreadAnalysisRunner;
import pg.edu.pl.lsea.gui.analysis.displays.datadisplays.OutputTableDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.DefaultDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.results.TopNModels;
import pg.edu.pl.lsea.gui.analysis.displays.results.TopNOperators;
import pg.edu.pl.lsea.gui.analysis.displays.results.TopNOperatorsPercentage;
import pg.edu.pl.lsea.gui.analysis.utils.AircraftParser;
import pg.edu.pl.lsea.gui.analysis.utils.DataStorageAnalysis;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class responsible for showing the analysis area
 */
public class AnalysisPanel extends JPanel {
    private DataStorage dataStorage;
    private AnalysisDisplay currentDisplay;
    private AircraftParser parser;
    private JTextArea progressTextArea;

    /**
     * Sets FlightData if available and updated the display
     * @param flights list of flights to be added to storage.
     */
    public void setFlightData(List<Flight> flights) {
        for (Flight flight : flights) {
            dataStorage.addFlight(flight);
        }
    }

    /**
     * Sets the AircraftData if available and updates the display
     * @param aircrafts list of aircrafts to be added to storage.
     */
    public void setAircraftData(List<Aircraft> aircrafts) {
        for (Aircraft aircraft : aircrafts) {
            dataStorage.addAircraft(aircraft);
        }
    }

    /**
     * Function initilizing the parser of aircrafts.
     * Parser maps Icao24 to aircrafts.
     */
    public void initializeParser() {
        List<Aircraft> aircrafts = dataStorage.getAircrafts();
        parser = new AircraftParser(aircrafts);
    }

    /**
     * Constructor for AnalysisPanel.
     */
    public AnalysisPanel() {
        setLayout(null); // Can be later changed to other layout like FlowLayout
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        dataStorage = DataStorage.getInstance();
        currentDisplay = new DefaultDisplay();
        add(currentDisplay);

        progressTextArea = new JTextArea();
        progressTextArea.setEditable(false);
        progressTextArea.setBounds(10, 10, 400, 200);
        progressTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(progressTextArea);
    }

    public void updateAnalysisProgress(String message) {
        progressTextArea.append(message + "\n");
        progressTextArea.setCaretPosition(progressTextArea.getDocument().getLength());
    }

    /**
     * Fully remove the display from analysis panel, and then repaint.
     */
    public void removeDisplay() {
        if (currentDisplay != null) {
            remove(currentDisplay);
        }
        repaint();
    }

    /**
     * The window that will make choosing number of threads possible.
     * Options: 1, 2, 4, 8, 16.
     * Will return 4 thread as default.
     * @return int - number of threads.
     */
    private int promptForThreadCount() {
        Integer[] options = {1, 2, 4, 8, 16};
        Integer selection = (Integer) JOptionPane.showInputDialog(
                null,
                "Select number of threads for parallel analysis:",
                "Thread Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                NUMBER_OF_DEFAULT_THREADS // default
        );

        return selection != null ? selection : NUMBER_OF_DEFAULT_THREADS; // default to 4 if user cancels
    }


    /**
     * Perform analysis on available data.
     * <ul>
     *   <li> 1 - Sequential vs Parallel performance</li>
     *   <li> 2 - Sort by number of flights</li>
     *   <li> 3 - Sort by total time of flights</li>
     *   <li> 4 - Most popular operators</li>
     *   <li> 5 - Most popular models</li>
     *   <li> 6 - Plot average time</li>
     *   <li> 7 - Flights per airport</li>
     *   <li> 8 - Most used operators</li>
     * </ul>
     * @param analysisType int - type of the analysis to be performed
     */
    public void performAnalysis(int analysisType) {
        progressTextArea.setText("");
        removeDisplay();
        SortingCalculator calc = new SortingCalculator();

        switch (analysisType) {
            case THREADS_ANALYSIS:
                performThreadsAnalysis();
                break;
            case SORT_BY_NUMBER_OF_FLIGHTS:
                performSortByNumberOfFlights(calc);
                break;
            case SORT_BY_TOTAL_TIME_OF_FLIGHTS:
                performSortByTimeOfFlights(calc);
                break;
            case MOST_POPULAR_OPERATORS:
                performTopNOperators(calc);
                break;
            case MOST_POPULAR_MODELS:
                performTopNModels(calc);
                break;
            case PLOT_AVERAGE_TIME:
                performPlotAverageTimePerOperator(calc);
                break;
            case FLIGHTS_PER_AIRPORT:
                performPercentageOfLongFlights(calc);
            default:
                currentDisplay = new DefaultDisplay();
                add(currentDisplay);
        }
        repaint();
    }

    private void performPlotAverageTimePerOperator(SortingCalculator calc) {
        System.out.println("Showing plot...");
        PlotAverageTimePerOperatorDisplay plotDisplay = new PlotAverageTimePerOperatorDisplay(parser);
        plotDisplay.plotAverageTime(calc);
        System.out.println("Stop showing");
    }

    private void performTopNOperators(SortingCalculator calc) {
        System.out.println("Showing TopN operators...");
        TopNOperators topNOperatorsAnalyse = new TopNOperators();
        topNOperatorsAnalyse.analyzeTopOperators(calc, parser);
        topNOperatorsAnalyse.showResultsDialog();
    }

    private void performTopNModels(SortingCalculator calc) {
        System.out.println("Showing TopN models...");
        TopNModels topNModelsAnalyse = new TopNModels();
        topNModelsAnalyse.analyzeTopModels(calc, parser);
        topNModelsAnalyse.showResultsDialog();
    }

    /**
     * Perform the threads analysis - sequential and multithread.
     * Shows a window to choose number of threads and then runs analysis.
     * The information about time needed to perform it will be shown.
     */
    private void performThreadsAnalysis() {
        System.out.println("Comparing sequential and parallel performance...");
        ThreadAnalysisRunner runner = new ThreadAnalysisRunner(this);

        int threads = promptForThreadCount();
        // Uncomment bellow to perform single-thread analysis always
        // runner.runAnalysis(false, 1);  // Single-threaded
        //runner.runAnalysis(true, threads);   // Multi-threaded

        new Thread(() -> {
            runner.runAnalysis(true, threads); // Multi-threaded analysis
            runner.showResultsDialog();
        }).start();
    }

    private void performPercentageOfLongFlights(SortingCalculator calc) {
        System.out.println("Long Flights percentage...");
        TopNOperatorsPercentage topPercentage = new TopNOperatorsPercentage();
        topPercentage.analyzeTopOperators(calc, parser);
        topPercentage.showResultsDialog();
    }

    private void performSortByNumberOfFlights(SortingCalculator calc) {
        List <EnrichedFlight> enrichedFlights = DataStorageAnalysis.prepareFlights();
        System.out.println("Sorting by the amount of flights...");
        List<Output> sortedByCount = calc.sortByAmountOfFlights(enrichedFlights);
        displaySortedData(sortedByCount, "amount of flights");
    }

    private void performSortByTimeOfFlights(SortingCalculator calc) {
        List <EnrichedFlight> enrichedFlights = DataStorageAnalysis.prepareFlights();
        System.out.println("Sorting by the time of flights...");
        List<Output> sortedByTime = calc.sortByTimeOfFlights(enrichedFlights);
        displaySortedData(sortedByTime, "time of flights");
    }

    private void displaySortedData(List<Output> sortedData, String description) {
        currentDisplay = new OutputTableDisplay(sortedData, parser);
        currentDisplay.showAsPopup(description);
    }
}
