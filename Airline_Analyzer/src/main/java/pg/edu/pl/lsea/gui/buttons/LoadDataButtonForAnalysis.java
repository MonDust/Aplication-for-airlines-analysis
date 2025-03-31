package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.filechoice.ChoosingFileWindow;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class LoadDataButtonForAnalysis extends LoadDataButton{
    public LoadDataButtonForAnalysis(MainPanel panel){
        super(panel);

        setText("Load Data");
        setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Function opening the file chooser window
     */
    private void openFileChooserWindow() {
        // This will open the ChoosingFileWindow as a separate window
        ChoosingFileWindow fileWindow = new ChoosingFileWindow(mainPanel);
    }

    /**
     * Action performed when clicking on the button - loading of the flight data from cvs file
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        openFileChooserWindow();
    }
}
