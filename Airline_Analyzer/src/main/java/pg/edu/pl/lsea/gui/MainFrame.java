package pg.edu.pl.lsea.gui;

import javax.swing.*;
import java.awt.Color;

public class MainFrame extends JFrame {
    public MainFrame(MainPanel mainPanel) {
        this.add(mainPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.white);
        this.setVisible(true);
    }
}
