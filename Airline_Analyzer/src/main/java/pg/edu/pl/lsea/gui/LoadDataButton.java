package pg.edu.pl.lsea.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadDataButton extends JButton implements ActionListener {
    private final MainPanel mainPanel;
    public LoadDataButton(MainPanel panel) {
        mainPanel = panel;

        // TODO
//        setBounds();                     // button coordinates should take from utils/Constants file
//        setText("Load data");
//        setFont()
        setFocusable(false);
        addActionListener(this);
    }

    // TODO
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
