package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

/**
 * Base class for all Graphical Analysis components
 */
public abstract class AnalysisArea extends JPanel {
    private java.util.List<Flight> flightData;
    private List<Aircraft> aircraftData;

    /**
     * Constructor of the class.
     * Sets the default location of the area and creates border
     */
    public AnalysisArea() {
        setBounds(ANALYSIS_X + 10, ANALYSIS_Y + 10, 200, 200);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
