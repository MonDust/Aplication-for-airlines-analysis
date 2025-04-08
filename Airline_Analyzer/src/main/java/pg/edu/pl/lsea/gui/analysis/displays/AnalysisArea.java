package pg.edu.pl.lsea.gui.analysis.displays;

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
    private List<Flight> flightData;
    private List<Aircraft> aircraftData;

    /**
     * Constructor of the class.
     * Sets the default location of the area and creates border
     */
    public AnalysisArea() {
        setBounds(0, 0 , ANALYSIS_WIDTH, ANALYSIS_HEIGHT);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Launch the analysis area as a pop-up window.
     * @param title - The title of the pop-up.
     */
    public void showAsPopup(String title) {
        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setTitle(title);

        // Setup the dialog content
        dialog.setContentPane(this);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
