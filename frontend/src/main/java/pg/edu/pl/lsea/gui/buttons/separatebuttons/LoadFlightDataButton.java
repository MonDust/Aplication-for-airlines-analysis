package pg.edu.pl.lsea.gui.buttons.separatebuttons;

import pg.edu.pl.lsea.gui.buttons.BaseDataButton;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class representing button for loading the flight data
 */
public class LoadFlightDataButton extends BaseDataButton {
    /**
     * Constructor for the LoadFlightDataButton class
     * @param panel - main panel
     */
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

//        TODO - REMOVE this part - this code is never executed when loading data
//        System.out.println("TEST - LoadFlightDataButton - actionPerformed(ActionEvent e)");
//        File file = chooseFile();
//        CsvDataLoader dataLoader = new CsvDataLoader();
//        if (file != null) {
//            dataLoader.loadFlightsToStorage(file);
//        }
    }
}
