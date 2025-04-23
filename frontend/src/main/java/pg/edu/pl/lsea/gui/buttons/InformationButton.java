package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.choicewindows.BaseChoosingWindow;
import pg.edu.pl.lsea.gui.choicewindows.ChoosingInformationWindow;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.BUTTON_HEIGHT;

/**
 * Information Button - which will show information choice window after clicking
 */
public class InformationButton extends BaseDataButton {
    /**
     * Constructor for InformationButton class
     * @param panel - main panel
     */
    public InformationButton(MainPanel panel){
        super(panel);

        setText("Information");
        setBounds(BUTTON_X + 2 * (BUTTON_WIDTH + ADDITIONAL_SPACE), BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - choosing the information to show
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // This will open the ChoosingInformationWindow as a separate window
        BaseChoosingWindow informationWindow = new ChoosingInformationWindow(mainPanel);
    }
}
