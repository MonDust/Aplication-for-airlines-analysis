package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.MainPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class LoadFlightDataButton extends LoadDataButton {
    public LoadFlightDataButton(MainPanel panel) {
        super(panel);

        setText("Load Flight Data (CSV)");
        setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(mainPanel, "Loading flight data...");
    }
}
