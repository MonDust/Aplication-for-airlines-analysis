package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.choicewindows.BaseChoosingWindow;
import pg.edu.pl.lsea.gui.choicewindows.ChoosingAnalysisWindow;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.BUTTON_HEIGHT;

/**
 * Load Analysis Button - which will show analysis choice window after clicking
 */
public class LoadAnalysisButton extends LoadDataButton{
    /**
     * Constructor for LoadAnalysisButton class
     * @param panel - main panel
     */
    public LoadAnalysisButton(MainPanel panel){
        super(panel);

        setText("Analyze");
        setBounds(BUTTON_X + BUTTON_WIDTH + ADDITIONAL_SPACE, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Action performed when clicking on the button - choosing the analysis to be performed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainPanel.initializeParser();
        // This will open the ChoosingFileWindow as a separate window
        BaseChoosingWindow fileWindow = new ChoosingAnalysisWindow(mainPanel);
    }
}
