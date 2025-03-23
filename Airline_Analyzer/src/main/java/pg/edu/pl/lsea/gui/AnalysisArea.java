package pg.edu.pl.lsea.gui;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class AnalysisArea extends JPanel {
    public AnalysisArea() {
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

    }

    // Example graph - to change when analysis will be possible
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Analysis Area", 10, 20);
        g.fillRect(20, 50, 30, 40);
        g.fillRect(60, 50, 30, 60);
        g.fillRect(100, 50, 30, 30);
    }
}