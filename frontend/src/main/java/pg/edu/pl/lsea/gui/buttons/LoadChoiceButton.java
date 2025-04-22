package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.choicewindows.BaseChoosingWindow;
import pg.edu.pl.lsea.gui.choicewindows.ChoosingFileWindow;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Load Choice Button class - which will show the choice window after clicking
 */
public class LoadChoiceButton extends BaseDataButton {
    /**
     * Constructor for LoadChoiceButton class
     * @param panel - main panel
     */
    public LoadChoiceButton(MainPanel panel){
        super(panel);

        setText("Load Data");
        setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - choosing the file type to load the data from and type of data that will be loaded
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // This will open the ChoosingFileWindow as a separate window
        BaseChoosingWindow fileWindow = new ChoosingFileWindow(mainPanel);
    }
}
