package pg.edu.pl.lsea.gui;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class LoadDataButton extends JButton implements ActionListener {
    private final MainPanel mainPanel;
    private final JButton flightDataButton;
    private final JButton aircraftDataButton;

    public LoadDataButton(MainPanel panel) {
        mainPanel = panel;

        flightDataButton = new JButton("Load Flight Data (CSV)");
        flightDataButton.setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        flightDataButton.setFont(new Font("Arial", Font.BOLD, 14));
        flightDataButton.setFocusable(false);
        flightDataButton.addActionListener(this);

        aircraftDataButton = new JButton("Load Aircraft Data (CSV)");
        aircraftDataButton.setBounds(BUTTON_X + BUTTON_WIDTH + 10, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        aircraftDataButton.setFont(new Font("Arial", Font.BOLD, 14));
        aircraftDataButton.setFocusable(false);
        aircraftDataButton.addActionListener(this);
    }

    public JButton getFlightDataButton() {
        return flightDataButton;
    }

    public JButton getAircraftDataButton() {
        return aircraftDataButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == flightDataButton) {
            JOptionPane.showMessageDialog(mainPanel, "Loading flight data...");
        } else if (e.getSource() == aircraftDataButton) {
            JOptionPane.showMessageDialog(mainPanel, "Loading aircraft data...");
        }
    }
}
