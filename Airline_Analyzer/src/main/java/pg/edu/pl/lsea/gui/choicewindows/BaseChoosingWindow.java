package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;
import java.awt.*;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.CHOICE_WINDOW_HEIGHT;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.CHOICE_WINDOW_WIDTH;

/**
 * Base Choosing Window class
 */
public abstract class BaseChoosingWindow extends JFrame {
    final protected MainPanel mainPanel;

    /**
     * Constructor of the class
     * @param panel - main panel
     */
    public BaseChoosingWindow(MainPanel panel) {
        this.mainPanel = panel;
        setLayout(new FlowLayout());
        setSize(CHOICE_WINDOW_WIDTH, CHOICE_WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
