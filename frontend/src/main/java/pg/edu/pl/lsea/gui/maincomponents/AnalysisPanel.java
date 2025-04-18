package pg.edu.pl.lsea.gui.maincomponents;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class AnalysisPanel extends JPanel {
    private final CardLayout cardLayout;
    private final Map<String, JPanel> analysisViews = new HashMap<>();

    public AnalysisPanel() {
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
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Select an analysis option", SwingConstants.CENTER);
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
        switch (analysisType) {
            case PERFORM_ALL_TYPES -> display = createPerformanceDisplay();
            case SORT_BY_NUMBER_OF_FLIGHTS -> display = createSortedFlightsDisplay();
            case SORT_BY_TOTAL_TIME_OF_FLIGHTS -> display = createTotalFlightTimeDisplay();
            case MOST_POPULAR_OPERATORS -> display = createCorrelationDisplay();
            case MOST_POPULAR_MODELS -> display = createFlightsPerAircraftDisplay();
            case PLOT_AVERAGE_TIME -> display = createGroupByOperatorDisplay();
            case PERCENTAGE_OF_THE_LONG_FLIGHTS -> display = createFlightsPerAirportDisplay();
            default -> {
                showDisplay("welcome");
                return;
            }
        }
        addAnalysisDisplay(key, display);
        showDisplay(key);
    }


    private JPanel createMostUsedOperatorsDisplay() {
        return createPlaceholderPanel("Most Used Operators Display");
    }


    private JPanel createAverageTimeChartDisplay() {
        return createPlaceholderPanel("Average Time Chart Display");
    }

    private JPanel createPerformanceDisplay() {
        return createPlaceholderPanel("Performance Display");
    }

    private JPanel createSortedFlightsDisplay() {
        return createPlaceholderPanel("Sorted Flights Display");
    }

    private JPanel createTotalFlightTimeDisplay() {
        return createPlaceholderPanel("Total Flight Time Display");
    }

    private JPanel createCorrelationDisplay() {
        return createPlaceholderPanel("Correlation Display");
    }

    private JPanel createFlightsPerAircraftDisplay() {
        return createPlaceholderPanel("Flights Per Aircraft Display");
    }

    private JPanel createGroupByOperatorDisplay() {
        return createPlaceholderPanel("Group By Operator Display");
    }

    private JPanel createFlightsPerAirportDisplay() {
        return createPlaceholderPanel("Flights Per Airport Display");
    }

    /**
     * A placeholder panel - a default panel in a way, makes a panel when no data available.
     * @param title
     * @return
     */
    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title + " (not implemented)", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.ITALIC, 14));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }


}
