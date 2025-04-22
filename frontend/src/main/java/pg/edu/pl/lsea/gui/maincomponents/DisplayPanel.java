package pg.edu.pl.lsea.gui.maincomponents;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.display.datadisplay.tables.TableDisplay;
import pg.edu.pl.lsea.gui.display.graphdisplay.PlotAverageTimePerOperatorDisplay;
import pg.edu.pl.lsea.gui.display.topNdisplay.TopNModelsDisplay;
import pg.edu.pl.lsea.gui.display.topNdisplay.TopNOperatorsDisplay;
import pg.edu.pl.lsea.gui.display.topNdisplay.TopNOperatorsPercentageDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;
import static pg.edu.pl.lsea.utils.InformationTypeConstants.*;

public class DisplayPanel extends JPanel {
    private final CardLayout cardLayout;
    private final Map<String, JPanel> analysisViews = new HashMap<>();

    public DisplayPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout); // you can keep the outer layout null if needed
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addAnalysisDisplay("welcome", createWelcomePanel());
        cardLayout.show(this, "welcome");
    }

    /**
     * Welcome panel
     * @return a welcom panel - JPanel
     */
    private JPanel createWelcomePanel() {
        return createPlaceholderPanel("Welcome");
    }

    /**
     * A placeholder panel - a default panel in a way, makes a panel when no data available.
     * @param title the text shown on placeholder panel
     * @return JPanel with placeholder panel
     */
    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public void addAnalysisDisplay(String key, JPanel display) {
        if (analysisViews.containsKey(key)) {
            // Remove old panel
            this.remove(analysisViews.get(key));
        }

        // Put new panel and re-add it to CardLayout
        analysisViews.put(key, display);
        this.add(display, key);
        cardLayout.show(this, key);
    }

    public void showDisplay(String key) {
        if (analysisViews.containsKey(key)) {
            cardLayout.show(this, key);
        }
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
     * Show analysis display for available data.
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
    public void displayAnalysis(int analysisType) {
        String key = "analysis_" + analysisType;

        if (analysisViews.containsKey(key)) {
            showDisplay(key);
            return;
        }

        JPanel display;
        // List<Aircraft> aircraftList = reader.readAircrafts(new File("C:\\Users\\maria\\Desktop\\PG\\SEM_VI\\Large-scale_enterprise_application\\Project\\Main_test_files\\aircraft-database-complete-2022-09.csv"));
        // List<Flight> flightList = reader.readFlights(new File("C:\\Users\\maria\\Desktop\\PG\\SEM_VI\\Large-scale_enterprise_application\\Project\\Main_test_files\\flight_sample_2022-09-01.csv"));

        switch (analysisType) {
            case PERFORM_ALL_TYPES -> {
                createPerformAllTypesDisplay();
                return;
            }
            case MOST_POPULAR_OPERATORS -> display = createMostPopularOperatorsDisplay();
            case MOST_POPULAR_MODELS -> display = createMostPopularModelsDisplay();
            case PLOT_AVERAGE_TIME -> display = createPlotAverageTimeDisplay();
            case PERCENTAGE_OF_THE_LONG_FLIGHTS -> display = createPercentageOfTheLongFlightsDisplay();
            default -> {
                showDisplay("welcome");
                return;
            }
        }
        addAnalysisDisplay(key, display);
        showDisplay(key);
    }


    private void createPerformAllTypesDisplay() {
        displayAnalysis(MOST_POPULAR_OPERATORS);
        displayAnalysis(MOST_POPULAR_MODELS);
        displayAnalysis(PLOT_AVERAGE_TIME);
        displayAnalysis(PERCENTAGE_OF_THE_LONG_FLIGHTS);
    }

    private JPanel createMostPopularOperatorsDisplay() {
        return new TopNOperatorsDisplay();
    }

    private JPanel createMostPopularModelsDisplay() {
        return new TopNModelsDisplay();
    }

    private JPanel createPlotAverageTimeDisplay() {
        return new PlotAverageTimePerOperatorDisplay();
    }

    private JPanel createPercentageOfTheLongFlightsDisplay() {
        return new TopNOperatorsPercentageDisplay();
    }


    public void showInformation(int informationType) {
        String key = "information_" + informationType;

        if (analysisViews.containsKey(key)) {
            showDisplay(key);
            return;
        }

        JPanel display;

        switch (informationType) {
            case RECORDS_SIZE -> display = showRecordsSizeInformation();
            case FLIGHTS_INFORMATION -> display = showFlightsInformation();
            case AIRCRAFTS_INFORMATION -> display = showAircraftsInformation();
            default -> {
                showDisplay("welcome");
                return;
            }
        }
        addAnalysisDisplay(key, display);
        showDisplay(key);
    }

    public JPanel showFlightsInformation() {
        // TODO - get info by API
        List<Aircraft> aircraftList = new ArrayList();
        List<Flight> flightList = new ArrayList();
        return createPlaceholderPanel("Size information: Flights: " + flightList.size() + " | " + "Aircrafts: " + aircraftList.size() );
    }

    public JPanel showAircraftsInformation() {
        // TODO - get info by API
        List<Aircraft> aircraftList = new ArrayList();
        return new TableDisplay(aircraftList);
    }

    public JPanel showRecordsSizeInformation() {
        // TODO - get info by API
        List<Flight> flightList = new ArrayList();
        return new TableDisplay(flightList);
    }

}
