package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Base class for all available buttons
 */
public abstract class LoadDataButton extends JButton implements ActionListener {
    protected final MainPanel mainPanel;


    /**
     * Constructor of the class
     * @param panel  - main panel
     */
    public LoadDataButton(MainPanel panel) {
        mainPanel = panel;
        setFont(new Font("Arial", Font.BOLD, 14));
        setFocusable(false);
        addActionListener(this);
    }

    /**
     * Choose file from the available
     * @return - chosen file to load the data
     */
    protected File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
