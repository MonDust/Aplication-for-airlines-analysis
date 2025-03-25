package pg.edu.pl.lsea.gui.analysis;

import java.awt.*;

/**
 * Class representing a basic chart
 */
public class Chart extends AnalysisArea {

    /**
     * Example graph - to change when analysis will be possible
     * @param g the Graphics object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Analysis Area", 10, 20);
        g.fillRect(20, 50, 30, 40);
        g.fillRect(60, 50, 30, 60);
        g.fillRect(100, 50, 30, 30);
    }
}
