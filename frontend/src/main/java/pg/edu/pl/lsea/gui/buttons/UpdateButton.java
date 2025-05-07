package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.choicewindows.BaseChoosingWindow;
import pg.edu.pl.lsea.gui.choicewindows.ChoosingAnalysisWindow;
import pg.edu.pl.lsea.gui.choicewindows.ChoosingUpdateWindow;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.BUTTON_HEIGHT;

public class UpdateButton  extends BaseDataButton{
    /**
     * Constructor for UpdateButton class
     * @param panel - main panel
     */
    public UpdateButton(MainPanel panel){
        super(panel);

        setText("Update Data");
        setBounds(BUTTON_X + 3 * (BUTTON_WIDTH + ADDITIONAL_SPACE), BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - choosing the update to be performed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // This will open the ChoosingUpdateWindow as a separate window
        BaseChoosingWindow fileWindow = new ChoosingUpdateWindow(mainPanel);
    }
}
