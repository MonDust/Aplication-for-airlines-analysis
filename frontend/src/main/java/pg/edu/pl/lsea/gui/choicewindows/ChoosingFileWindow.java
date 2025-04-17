package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataReader;
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
    final private CsvDataReader dataLoader = new CsvDataReader();

    /**
     * Create ChoosingFileWindow object.
     * Creates JLabels to choose file type and to choose data type, and a button to confirm.
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

        // Data Type Selection (flights or aircrafts)
        JLabel dataTypeLabel = new JLabel("Select data type:");
        // Available Data Types
        String[] dataTypes = new String[]{"Flights", "Aircrafts"};
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

        // Use last known directory if available
        File lastDir = mainPanel.getLastDirectory();
        if (lastDir != null && lastDir.exists()) {
            fileChooser.setCurrentDirectory(lastDir);
        }

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
        String dialogTitle = "Choose " + dataType + " file(s)";
        int returnValue = fileChooser.showDialog(this, dialogTitle);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();

            // Save the parent directory of the first selected file
            if (selectedFiles.length > 0) {
                mainPanel.setLastDirectory(selectedFiles[0].getParentFile());
            }

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

        System.out.println("TEST - ChoosingFileWindow - loadData(File file, String dataType)");

        int recordsLoaded;
        if ("Flights".equals(dataType)) {
            List<Flight> flights = dataLoader.readFlights(file);

            // TODO - send loaded objects through API
            // FIX it
            recordsLoaded = 0;
            JOptionPane.showMessageDialog(mainPanel, "Loaded " + recordsLoaded + " flights.");

            System.out.println("Loading flight data from " + file.getName());
        } else if ("Aircrafts".equals(dataType)) {
            List<Aircraft> aircrafts = dataLoader.readAircrafts(file);

            // TODO - send loaded objects through API
            // FIX it
            recordsLoaded = 0;
            JOptionPane.showMessageDialog(mainPanel, "Loaded " + recordsLoaded + " aircrafts.");

            System.out.println("Loading aircrafts data from " + file.getName());
        }
    }
}