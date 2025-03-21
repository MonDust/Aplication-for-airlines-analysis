package pg.edu.pl.lsea.gui;

import javax.swing.*;
import java.awt.*;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_HEIGHT;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_WIDTH;

public class MainPanel extends JPanel {
    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.white);


//         TODO - uncomment this after implementing Button
//        LoadDataButton loadDataButton = new LoadDataButton(this);
//        add(loadDataButton);
    }
}
