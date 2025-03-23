package pg.edu.pl.lsea.gui;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class LoadDataButton extends JButton implements ActionListener {
    private final MainPanel mainPanel;

    public LoadDataButton(MainPanel panel) {
        mainPanel = panel;

        setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        setText("Load Data");
        setFont(new Font("Arial", Font.BOLD, 14));
        setFocusable(false);
        addActionListener(this);
    }

    // TODO
    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(mainPanel, "Loading data...");
    }
}
