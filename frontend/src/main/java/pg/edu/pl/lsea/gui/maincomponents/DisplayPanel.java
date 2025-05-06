package pg.edu.pl.lsea.gui.maincomponents;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import pg.edu.pl.lsea.api.DataLoader;
import pg.edu.pl.lsea.api.DataUpdateDelete;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.ProgressBarUDP;
import pg.edu.pl.lsea.gui.display.datadisplay.tables.TableDisplay;
import pg.edu.pl.lsea.gui.display.graphdisplay.PlotAverageTimePerOperatorDisplay;
import pg.edu.pl.lsea.gui.display.topNdisplay.TopNModelsDisplay;
import pg.edu.pl.lsea.gui.display.topNdisplay.TopNOperatorsDisplay;
import pg.edu.pl.lsea.gui.display.topNdisplay.TopNOperatorsPercentageDisplay;
import pg.edu.pl.lsea.utils.DateLabelFormatter;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;
import static pg.edu.pl.lsea.utils.InformationTypeConstants.*;
import static pg.edu.pl.lsea.utils.UpdateTypeConstants.*;

/**
 * Class for displaying information (adding displays) with cardLayout.
 * Previously: AnalysisPanel -> shows both analysis and information about the data.
 */
public class DisplayPanel extends JPanel {
    private final CardLayout cardLayout;
    private final Map<String, JPanel> analysisViews = new HashMap<>();
    DataLoader dataLoader = new DataLoader();
    DataUpdateDelete dataUpdateDelete = new DataUpdateDelete();

    /**
     * Constructor for the class.
     * Adds basic display - "welcome".
     */
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

    /**
     * Function for adding the display.
     * @param key - the key of the analysis display, a way to choose the analysis display to show.
     *            the same way it is possible to add information display.
     * @param display - the display that is supposed to be added, and recognized by the passed key.
     */
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

    /**
     * Show the display by inputting the key as an argument. It will show corresponding display.
     * @param key - the key of display that is supposed to show.
     */
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

        JPanel display;
        // List<Aircraft> aircraftList = reader.readAircrafts(new File("C:\\Users\\maria\\Desktop\\PG\\SEM_VI\\Large-scale_enterprise_application\\Project\\Main_test_files\\aircraft-database-complete-2022-09.csv"));
        // List<Flight> flightList = reader.readFlights(new File("C:\\Users\\maria\\Desktop\\PG\\SEM_VI\\Large-scale_enterprise_application\\Project\\Main_test_files\\flight_sample_2022-09-01.csv"));
        new Thread(ProgressBarUDP::new).start();
        System.out.println("here 1");

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


    /**
     * Perform all types of analysis, and show the appropriate displays.
     */
    private void createPerformAllTypesDisplay() {
        displayAnalysis(MOST_POPULAR_OPERATORS);
        displayAnalysis(MOST_POPULAR_MODELS);
        displayAnalysis(PLOT_AVERAGE_TIME);
        displayAnalysis(PERCENTAGE_OF_THE_LONG_FLIGHTS);
    }

    /**
     * Show most popular operators - return the display.
     * @return JPanel with most popular operators.
     */
    private JPanel createMostPopularOperatorsDisplay() {
        return new TopNOperatorsDisplay();
    }

    /**
     * Show most popular models - return the display.
     * @return JPanel with most popular models.
     */
    private JPanel createMostPopularModelsDisplay() {
        return new TopNModelsDisplay();
    }

    /**
     * Plot average flight times for operators.
     * @return JPanle with the plot.
     */
    private JPanel createPlotAverageTimeDisplay() {

        PlotAverageTimePerOperatorDisplay averageTimePerOperatorDisplay = new PlotAverageTimePerOperatorDisplay();
        return averageTimePerOperatorDisplay.
                plotAverageTime();
    }

    /**
     * Show percentage of long flights for most popular operators.
     * @return JPanel with the percentages.
     */
    private JPanel createPercentageOfTheLongFlightsDisplay() {
        return new TopNOperatorsPercentageDisplay();
    }


    /**
     * Functions showing information depending on chosen option.
     * RECORD_SIZE - number of flights and number of aircrafts.
     * FLIGHTS_INFORMATION - All flights.
     * AIRCRAFT_INFORMATION - All aircrafts.
     * @param informationType - option of information type.
     */
    public void showInformation(int informationType) {
        String key = "information_" + informationType;

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

    /**
     * Show flights information - all flights.
     * @return JPanel with the list of flights.
     */
    public JPanel showFlightsInformation() {
        List<Flight> flightList = dataLoader.getAllFlights();
        return new TableDisplay<>(flightList);
    }

    /**
     * Show aircraft information - all aircrafts.
     * @return JPanel with the list of aircrafts.
     */
    public JPanel showAircraftsInformation() {
        List<Aircraft> aircraftList = dataLoader.getAllAircrafts();
        return new TableDisplay<>(aircraftList);
    }

    /**
     * Show information about number of records available.
     * @return JPanel with information.
     */
    public JPanel showRecordsSizeInformation() {
        List<Aircraft> aircraftList = dataLoader.getAllAircrafts();
        List<Flight> flightList = dataLoader.getAllFlights();
        return createPlaceholderPanel("Size information: Flights: " + flightList.size() + " | " + "Aircrafts: " + aircraftList.size() );
    }

    public void makeUpdate(int updateType) {
        String key = "update_" + updateType;

        JPanel display;

        switch (updateType) {
            case UPDATE_A -> display = showUpdateAircraftPanel();
            case UPDATE_F -> display = showUpdateFlightPanel();
            case DELETE_A -> display = showDeleteAircraftPanel();
            case DELETE_F -> display = showDeleteFlightsByDatePanel();
            default -> {
                showDisplay("welcome");
                return;
            }
        }
        addAnalysisDisplay(key, display);
        showDisplay(key);
    }

    public JPanel showUpdateFlightPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField icaoField = new JTextField();

        UtilDateModel fromDateModel = new UtilDateModel();
        UtilDateModel toDateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromDateModel, p);
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toDateModel, p);
        JDatePickerImpl fromDatePicker = new JDatePickerImpl(fromDatePanel, new DateLabelFormatter());
        JDatePickerImpl toDatePicker = new JDatePickerImpl(toDatePanel, new DateLabelFormatter());

        JTextField departureAirportField = new JTextField();
        JTextField arrivalAirportField = new JTextField();
        JTextField fromDateText = new JTextField();

        JButton updateBtn = new JButton("Update Flight");

        updateBtn.addActionListener(e -> {
            try {
                String icao = icaoField.getText().trim();
                String departure = departureAirportField.getText().trim();
                String arrival = arrivalAirportField.getText().trim();

                int from;
                try {
                    from = Integer.parseInt(fromDateText.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Date fromDate = (Date) fromDatePicker.getModel().getValue();
                Date toDate = (Date) toDatePicker.getModel().getValue();

                if (toDate == null) {
                    JOptionPane.showMessageDialog(panel, "Please select both dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // convert milliseconds to seconds
                int firstSeen = from;
                // int firstSeen = (int) (fromDate.getTime() / 1000);
                int lastSeen = (int) (toDate.getTime() / 1000);

                Flight flight = new Flight(icao, firstSeen, lastSeen, departure, arrival);

                // TODO
                System.out.println("Info: " + flight.getFirstSeen() + " " + flight.getIcao24());

                dataUpdateDelete.patchFlightByIcao24AndFirstSeen(flight.getIcao24(),flight.getFirstSeen(), flight);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Timestamps must be valid integers (Unix seconds).", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Flight ICAO24:"));
        panel.add(icaoField);
        panel.add(new JLabel("First Seen (Unix Time):"));
        panel.add(fromDateText);
        panel.add(new JLabel("Last Seen (Unix Time):"));
        panel.add(toDatePicker);
        panel.add(new JLabel("Departure Airport (IATA):"));
        panel.add(departureAirportField);
        panel.add(new JLabel("Arrival Airport (IATA):"));
        panel.add(arrivalAirportField);
        panel.add(updateBtn);

        return panel;
    }

    public JPanel showUpdateAircraftPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField icaoField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField operatorField = new JTextField();
        JTextField ownerField = new JTextField();

        JButton updateBtn = new JButton("Update Aircraft");

        updateBtn.addActionListener(e -> {
            String icao = icaoField.getText().trim();
            String model = modelField.getText().trim();
            String operator = operatorField.getText().trim();
            String owner = ownerField.getText().trim();

            Aircraft aircraft = new Aircraft(icao, model, operator, owner);

            // TODO
            dataUpdateDelete.patchAircraft(aircraft.getIcao24(), aircraft);
        });

        panel.add(new JLabel("Aircraft ICAO24:"));
        panel.add(icaoField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Operator:"));
        panel.add(operatorField);
        panel.add(new JLabel("Owner:"));
        panel.add(ownerField);
        panel.add(updateBtn);

        return panel;
    }

    public JPanel showDeleteFlightsByDatePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        UtilDateModel fromDateModel = new UtilDateModel();
        UtilDateModel toDateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromDateModel, p);
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toDateModel, p);
        JDatePickerImpl fromDatePicker = new JDatePickerImpl(fromDatePanel, new DateLabelFormatter());
        JDatePickerImpl toDatePicker = new JDatePickerImpl(toDatePanel, new DateLabelFormatter());

        JTextField fromDateText = new JTextField();

        JButton deleteBtn = new JButton("Delete Flights");

        deleteBtn.addActionListener(e -> {
            Date fromDate = (Date) fromDatePicker.getModel().getValue();
//            int from;
//            try {
//                from = Integer.parseInt(fromDateText.getText().trim());
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(panel, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }


            Date toDate = (Date) toDatePicker.getModel().getValue();

            if (fromDate == null || toDate == null) {
                JOptionPane.showMessageDialog(panel, "Please select both dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // convert milliseconds to seconds
            int from = (int) (fromDate.getTime() / 1000);
            int to = (int) (toDate.getTime() / 1000);

            System.out.println("Info: " + from + " " + to);

            // TODO
            dataUpdateDelete.deleteFlightsByDateRange(from, to);
        });

        panel.add(new JLabel("From Date:"));
        //panel.add(fromDateText);
        panel.add(fromDatePicker);
        panel.add(new JLabel("To Date:"));
        panel.add(toDatePicker);
        panel.add(deleteBtn);

        return panel;
    }

    public JPanel showDeleteAircraftPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField icaoField = new JTextField();

        JButton deleteBtn = new JButton("Delete Aircraft");

        deleteBtn.addActionListener(e -> {
            String icao = icaoField.getText().trim();
            dataUpdateDelete.deleteAircraft(icao);
        });

        panel.add(new JLabel("Aircraft ICAO24:"));
        panel.add(icaoField);
        panel.add(deleteBtn);

        return panel;
    }
}
