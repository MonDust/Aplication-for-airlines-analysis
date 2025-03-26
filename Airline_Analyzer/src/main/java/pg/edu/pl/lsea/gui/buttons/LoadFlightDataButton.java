package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.MainPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class representing button for loading the flight data
 */
public class LoadFlightDataButton extends LoadDataButton {
    public LoadFlightDataButton(MainPanel panel) {
        super(panel);

        setText("Load Flight Data (CSV)");
        setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - loading of the flight data from cvs file
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        File file = chooseFile();
        if (file != null) {
            List<Flight> flights = dataLoader.loadFlights(file);
            JOptionPane.showMessageDialog(mainPanel, "Loaded " + flights.size() + " flights.");
            analysisPanel.setFlightData(flights);
        }
    }
}
