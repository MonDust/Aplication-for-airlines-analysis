package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.utils.AnalysisTypeConstants;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_DEFAULT_THREADS;

/**
 * Choosing Analysis Window which lets choose analysis type to perform
 * The class in which you choose and send request for the analysis.
 */
public class ChoosingAnalysisWindow extends BaseChoosingWindow{
    final private JComboBox<String> analysisTypeComboBox;

    /**
     * Constructor for ChoosingAnalysisWindow class.
     * Created JLable for choosing analysis type and a button to confirm.
     * @param panel - main panel
     */
    public ChoosingAnalysisWindow(MainPanel panel) {
        super(panel);
        setTitle("Choose Analysis Options");

        // Analysis Type Selection
        JLabel analysisTypeLabel = new JLabel("Select analysis type:");
        // Available File Types
        analysisTypeComboBox = new JComboBox<>(AnalysisTypeConstants.NAME_TO_ID.keySet().toArray(new String[0]));
        add(analysisTypeLabel);
        add(analysisTypeComboBox);

        // Start analysis button
        JButton loadButton = new JButton("Analyze");
        loadButton.addActionListener(e -> analyse());
        add(loadButton);

        // Set the window to be visible
        setVisible(true);
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
     * Perform analysis on available data based on the selected analysis type.
     <ul>
     *   <li> 1 - Sequential vs Parallel performance</li>
     *   <li> 2 - Sort by number of flights</li>
     *   <li> 3 - Sort by total time of flights</li>
     *   <li> 4 - Most popular operators</li>
     *   <li> 5 - Most popular models</li>
     *   <li> 6 - Plot average time</li>
     *   <li> 7 - Percentage of long flights</li>
     * </ul>
     */
    private void analyse() {
        String selectedName = (String) analysisTypeComboBox.getSelectedItem();
        int selectedType = AnalysisTypeConstants.NAME_TO_ID.getOrDefault(selectedName, 0);
        switch (selectedType) {
            case PERFORM_ALL_TYPES:
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
                break;
            default:
        }
        mainPanel.showAnalysis(selectedType);
    }

    /**
     * Generate a plot showing average time per top N Operators.
     */
    private void performPlotAverageTimePerOperator() {
        System.out.println("Showing plot...");
        //PlotAverageTimePerOperatorDisplay plotDisplay = new PlotAverageTimePerOperatorDisplay();

        // TODO - send request to API for analysis, pass results to the function below v
        //plotDisplay.plotAverageTime();
        System.out.println("Stop showing");
    }

    /**
     * Show Top N Operators.
     */
    private void performTopNOperators() {
        System.out.println("Showing TopN operators...");
        //TopNOperators topNOperators = new TopNOperators();

        // TODO - send request to API for analysis, pass results to the function below v
        //topNOperators.displayTopOperators();
        //topNOperators.showResultsDialog();
    }

    private void performTopNModels() {
        System.out.println("Showing TopN models...");
        //TopNModels topNModels = new TopNModels();


        // TODO - send request to API for analysis, pass results to the function below v
        //topNModels.displayTopModels();
        //topNModels.showResultsDialog();
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
        //TopNOperatorsPercentage topPercentage = new TopNOperatorsPercentage();

        // TODO - send request to API for analysis, pass results to the function below v
        //topPercentage.displayTopOperators();
        //topPercentage.showResultsDialog();
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
