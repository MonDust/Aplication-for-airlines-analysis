package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.MainPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class LoadAircraftDataButton extends LoadDataButton {
    public LoadAircraftDataButton(MainPanel panel) {
        super(panel);

        setText("Load Aircraft Data (CSV)");
        setBounds(BUTTON_X + BUTTON_WIDTH + 10, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(mainPanel, "Loading aircraft data...");
    }
}
