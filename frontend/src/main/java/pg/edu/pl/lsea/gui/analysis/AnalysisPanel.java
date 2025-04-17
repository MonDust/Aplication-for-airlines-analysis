package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.datadisplays.PlotAverageTimePerOperatorDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.DefaultDisplay;
import pg.edu.pl.lsea.gui.analysis.displays.results.TopNModels;
import pg.edu.pl.lsea.gui.analysis.displays.results.TopNOperators;
import pg.edu.pl.lsea.gui.analysis.displays.results.TopNOperatorsPercentage;

import javax.swing.*;
import java.awt.*;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class responsible for showing the analysis area
 */
public class AnalysisPanel extends JPanel {
//    private DataStorage dataStorage;
    private AnalysisDisplay currentDisplay;
//    private AircraftParser parser;
    private JTextArea progressTextArea;



    /**
     * Constructor for AnalysisPanel.
     * Getting instance of data storage, adding current display and text area.
     */
    public AnalysisPanel() {
        setLayout(null); // Can be later changed to other layout like FlowLayout
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        currentDisplay = new DefaultDisplay();
        add(currentDisplay);

        progressTextArea = new JTextArea();
        progressTextArea.setEditable(false);
        progressTextArea.setBounds(10, 10, 400, 200);
        progressTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(progressTextArea);
    }

    /**
     * Show the update of analysis in the text area.
     * @param message message that should be shown (update).
     */
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
        Integer[] options = {1, 2, 3, 4, 5, 10, 15, 20};
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
     *   <li> 7 - Percentage of long flights</li>
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
                performSortByNumberOfFlights();
                break;
            case SORT_BY_TOTAL_TIME_OF_FLIGHTS:
                performSortByTimeOfFlights();
                break;
            case MOST_POPULAR_OPERATORS:
                performTopNOperators();
                break;
            case MOST_POPULAR_MODELS:
                performTopNModels();
                break;
            case PLOT_AVERAGE_TIME:
                performPlotAverageTimePerOperator();
                break;
            case PERCENTAGE_OF_THE_LONG_FLIGHTS:
                performPercentageOfLongFlights();
            default:
                currentDisplay = new DefaultDisplay();
                add(currentDisplay);
        }
        repaint();
    }

    /**
     * Generate a plot showing average time per top N Operators.
     */
    private void performPlotAverageTimePerOperator() {
        System.out.println("Showing plot...");
        PlotAverageTimePerOperatorDisplay plotDisplay = new PlotAverageTimePerOperatorDisplay();

        // TODO - send request to API for analysis, pass results to the function below v
        plotDisplay.plotAverageTime();
        System.out.println("Stop showing");
    }

    /**
     * Show Top N Operators.
     */
    private void performTopNOperators() {
        System.out.println("Showing TopN operators...");
        TopNOperators topNOperators = new TopNOperators();

        // TODO - send request to API for analysis, pass results to the function below v
        topNOperators.displayTopOperators();
        topNOperators.showResultsDialog();
    }

    private void performTopNModels() {
        System.out.println("Showing TopN models...");
        TopNModels topNModels = new TopNModels();


        // TODO - send request to API for analysis, pass results to the function below v
        topNModels.displayTopModels();
        topNModels.showResultsDialog();
    }

    /**
     * Perform the threads analysis - sequential and multithread.
     * Shows a window to choose number of threads and then runs analysis.
     * The information about time needed to perform it will be shown.
     */
    private void performThreadsAnalysis() {
        System.out.println("Comparing sequential and parallel performance...");

        // TODO ask API for all analysis at once and display corresponding part after response of each 'version' of analysis
        // I removed the Thread analysis component completely, because from what I see there was mostly the analysis itself implemented
    }

    /**
     * Perform analysis to find the percentage of long flights for top N Operators.
     */
    private void performPercentageOfLongFlights() {
        System.out.println("Long Flights percentage...");
        TopNOperatorsPercentage topPercentage = new TopNOperatorsPercentage();

        // TODO - send request to API for analysis, pass results to the function below v
        topPercentage.displayTopOperators();
        topPercentage.showResultsDialog();
    }

    /**
     * Sorting and showing flights by the number and then showing the window.
     */
    private void performSortByNumberOfFlights() {
        // TODO - this part should be totally removed or simplified - it makes no sense to display the paginated table with all of the data we have in a database
        // If we really want to keep it, at least make these results grouped in some way
    }

    /**
     * Sorting and showing flights by the time and then showing the window.
     */
    private void performSortByTimeOfFlights() {
        // TODO - this part should be totally removed or simplified - it makes no sense to display the paginated table with all of the data we have in a database
        // If we really want to keep it, at least make these results grouped in some way
    }
}
