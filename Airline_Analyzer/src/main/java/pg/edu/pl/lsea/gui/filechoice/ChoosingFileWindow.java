package pg.edu.pl.lsea.gui.filechoice;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataLoader;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ChoosingFileWindow extends JFrame {
    final private MainPanel mainPanel;
    final private JComboBox<String> fileTypeComboBox;
    final private JComboBox<String> dataTypeComboBox;
    final private CsvDataLoader dataLoader = new CsvDataLoader();

    public ChoosingFileWindow(MainPanel panel) {
        this.mainPanel = panel;
        setTitle("Choose File Options");
        setLayout(new FlowLayout());
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // File Type Selection (for now only CSV)
        JLabel fileTypeLabel = new JLabel("Select file type:");
        fileTypeComboBox = new JComboBox<>(new String[]{"CSV"});
        add(fileTypeLabel);
        add(fileTypeComboBox);

        // Data Type Selection (flights or airports)
        JLabel dataTypeLabel = new JLabel("Select data type:");
        dataTypeComboBox = new JComboBox<>(new String[]{"Flights", "Airports"});
        add(dataTypeLabel);
        add(dataTypeComboBox);

        // Load File Button
        JButton loadButton = new JButton("Load Files");
        loadButton.addActionListener(e -> loadFiles());
        add(loadButton);

        // Set the window to be visible when this frame is shown
        setVisible(true);
    }

    private void loadFiles() {
        String fileType = (String) fileTypeComboBox.getSelectedItem();
        String dataType = (String) dataTypeComboBox.getSelectedItem();

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