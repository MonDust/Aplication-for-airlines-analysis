package pg.edu.pl.lsea.gui.analysis;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Class responsible for showing the analysis area
 */
public class AnalysisPanel extends JPanel {
    /**
     * Creates an AnalysisPanel object
     */
    public AnalysisPanel() {
        setLayout(null); // Can be later changed to other layout like FlowLayout
        setBounds(ANALYSIS_X, ANALYSIS_Y, ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        AnalysisArea analysisArea = new Chart();
        add(analysisArea);
    }

}
