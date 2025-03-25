package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class LoadDataButton extends JButton implements ActionListener {
    protected final MainPanel mainPanel;

    public LoadDataButton(MainPanel panel) {
        mainPanel = panel;
        setFont(new Font("Arial", Font.BOLD, 14));
        setFocusable(false);
        addActionListener(this);
    }
}
