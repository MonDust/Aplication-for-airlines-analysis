package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.gui.MainPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class representing button for loading the aircraft data
 */
public class LoadAircraftDataButton extends LoadDataButton {
    public LoadAircraftDataButton(MainPanel panel) {
        super(panel);

        setText("Load Aircraft Data (CSV)");
        setBounds(BUTTON_X + BUTTON_WIDTH + 10, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - loading of the aircraft data from cvs file
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        File file = chooseFile();
        if (file != null) {
            List<Aircraft> aircrafts = dataLoader.readAircrafts(file);
            JOptionPane.showMessageDialog(mainPanel, "Loaded " + aircrafts.size() + " aircrafts.");
            analysisPanel.setAircraftData(aircrafts);
        }
    }
}
