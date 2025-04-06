package pg.edu.pl.lsea.gui.maincomponents;

import javax.swing.*;
import java.awt.Color;

/**
 * Class representing the main and the only frame (window) in the GUI
 */
public class MainFrame extends JFrame {
    /**
     * Creates an object of MainFrame
     * @param mainPanel panel of the class MainPanel containing all other panels
     */
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
