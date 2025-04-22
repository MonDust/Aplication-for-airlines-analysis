package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.utils.AnalysisTypeConstants;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.*;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_DEFAULT_THREADS;

/**
 * Choosing Analysis Window which lets choose analysis type to perform
 * The class in which you choose and send request for the analysis.
 */
public class ChoosingAnalysisWindow extends BaseChoosingWindow{
    final private JComboBox<String> analysisTypeComboBox;

    /**
     * Constructor for ChoosingAnalysisWindow class.
     * Created JLable for choosing analysis type and a button to confirm.
     * @param panel - main panel
     */
    public ChoosingAnalysisWindow(MainPanel panel) {
        super(panel);
        setTitle("Choose Analysis Options");

        // Analysis Type Selection
        JLabel analysisTypeLabel = new JLabel("Select analysis type:");
        // Available File Types
        analysisTypeComboBox = new JComboBox<>(AnalysisTypeConstants.NAME_TO_ID.keySet().toArray(new String[0]));
        add(analysisTypeLabel);
        add(analysisTypeComboBox);

        // Start analysis button
        JButton loadButton = new JButton("Analyze");
        loadButton.addActionListener(e -> analyse());
        add(loadButton);

        // Set the window to be visible
        setVisible(true);
    }

    /**
     * Perform analysis on available data based on the selected analysis type.
     <ul>
     *   <li> 1 - Sequential vs Parallel performance</li>
     *   <li> 2 - Sort by number of flights</li>
     *   <li> 3 - Sort by total time of flights</li>
     *   <li> 4 - Most popular operators</li>
     *   <li> 5 - Most popular models</li>
     *   <li> 6 - Plot average time</li>
     *   <li> 7 - Percentage of long flights</li>
     * </ul>
     */
    private void analyse() {
        String selectedName = (String) analysisTypeComboBox.getSelectedItem();
        int selectedType = AnalysisTypeConstants.NAME_TO_ID.getOrDefault(selectedName, 0);
        mainPanel.showAnalysis(selectedType);
    }
}
