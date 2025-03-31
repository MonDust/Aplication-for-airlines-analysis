package pg.edu.pl.lsea.gui.maincomponents;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.gui.analysis.AnalysisPanel;
import pg.edu.pl.lsea.gui.buttons.LoadDataButton;
import pg.edu.pl.lsea.gui.buttons.LoadDataButtonForAnalysis;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_HEIGHT;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.WINDOW_WIDTH;

/**
 * Main Panel is the entire size of the Frame and contains other Panels
 */
public class MainPanel extends JPanel {
    // Data Button for loading the data - after which the window of choices will be shown
    private LoadDataButton mainLoadDataButton;
    private AnalysisPanel analysisPanel;

    /**
     * Create a MainPanel object
     */
    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.white);

        analysisPanel = new AnalysisPanel();
        add(analysisPanel);

        mainLoadDataButton = new LoadDataButtonForAnalysis(this);
        add(mainLoadDataButton);
    }

    public void setFlightData(List<Flight> flights) {
        analysisPanel.setFlightData(flights);
    }

    public void setAircraftData(List<Aircraft> flights) {
        analysisPanel.setAircraftData(flights);
    }


}
