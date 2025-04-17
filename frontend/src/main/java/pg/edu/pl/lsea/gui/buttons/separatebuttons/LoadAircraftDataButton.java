package pg.edu.pl.lsea.gui.buttons.separatebuttons;

import pg.edu.pl.lsea.gui.buttons.LoadDataButton;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class representing button for loading the aircraft data
 */
public class LoadAircraftDataButton extends LoadDataButton {
    /**
     * Constructor for the LoadAircraftDataButton class
     * @param panel - main panel
     */
    public LoadAircraftDataButton(MainPanel panel) {
        super(panel);

        setText("Load Aircraft Data (CSV)");
        setBounds(BUTTON_X + BUTTON_WIDTH + ADDITIONAL_SPACE, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - loading of the aircraft data from cvs file
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //        TODO - REMOVE this part - this code is never executed when loading data
//        File file = chooseFile();
//        CsvDataLoader dataLoader = new CsvDataLoader();
//        if (file != null) {
//            dataLoader.loadAircraftsToStorage(file);
//        }
    }
}
