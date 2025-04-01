package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataLoader;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * Choosing File Window which lets choose the searched file type and data type that will be gotten from it
 */
public class ChoosingFileWindow extends BaseChoosingWindow {
    final private JComboBox<String> fileTypeComboBox;
    final private JComboBox<String> dataTypeComboBox;
    final private CsvDataLoader dataLoader = new CsvDataLoader();

    /**
     * Create ChoosingFileWindow object.
     * @param panel - the main panel
     */
    public ChoosingFileWindow(MainPanel panel) {
        super(panel);
        setTitle("Choose File Options");

        // File Type Selection (for now only CSV)
        JLabel fileTypeLabel = new JLabel("Select file type:");
        // Available File Types
        String[] fileTypes = new String[]{"CSV"};
        fileTypeComboBox = new JComboBox<>(fileTypes);
        add(fileTypeLabel);
        add(fileTypeComboBox);

        // Data Type Selection (flights or airports)
        JLabel dataTypeLabel = new JLabel("Select data type:");
        // Available Data Types
        String[] dataTypes = new String[]{"Flights", "Airports"};
        dataTypeComboBox = new JComboBox<>(dataTypes);
        add(dataTypeLabel);
        add(dataTypeComboBox);

        // Load File Button
        JButton loadButton = new JButton("Load Files");
        loadButton.addActionListener(e -> loadFiles());
        add(loadButton);

        // Set the window to be visible when this frame is shown
        setVisible(true);
    }

    /**
     * Initialize file filter for searched types of files
     * @param fileType - the type of file needed
     * @return - return the fileChooser with needed options
     */
    private JFileChooser initializeFileFilter(String fileType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);

        switch (fileType) {
            case "CSV":
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                break;
            case null:
            default:
                fileChooser.setFileFilter(null);
        }
        return fileChooser;
    }

    /**
     * Initialize loading data with specific file options already given
     */
    private void loadFiles() {
        String fileType = (String) fileTypeComboBox.getSelectedItem();
        String dataType = (String) dataTypeComboBox.getSelectedItem();

        JFileChooser fileChooser = initializeFileFilter(fileType);

        // Open the file chooser dialog
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            // Validate the files based on selected file type and data type
            for (File file : selectedFiles) {
                loadData(file, dataType);
            }
        }
    }

    /**
     * Load the data from files according to choosen category
     * @param file - file to load data from
     * @param dataType - choosen category to load data to
     */
    private void loadData(File file, String dataType) {
        if ("Flights".equals(dataType)) {
            List<Flight> flightList = dataLoader.loadFlights(file);
            JOptionPane.showMessageDialog(mainPanel, "Loaded " + flightList.size() + " flights.");
            mainPanel.setFlightData(flightList);

            System.out.println("Loading flight data from " + file.getName());
        } else if ("Airports".equals(dataType)) {
            List<Aircraft> aircraftList = dataLoader.loadAircrafts(file);
            JOptionPane.showMessageDialog(mainPanel, "Loaded " + aircraftList.size() + " aircrafts.");
            mainPanel.setAircraftData(aircraftList);

            System.out.println("Loading airport data from " + file.getName());
        }
    }
}