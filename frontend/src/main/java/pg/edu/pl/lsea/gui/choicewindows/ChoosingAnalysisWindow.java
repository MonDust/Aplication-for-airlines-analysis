package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.utils.guispecific.optionmapping.AnalysisTypeConstants;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;

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
     *   <li> 1 - All analysis </li>
     *   <li> 2 - Most popular operators</li>
     *   <li> 3 - Most popular models</li>
     *   <li> 4 - Plot average time</li>
     *   <li> 5 - Percentage of long flights</li>
     * </ul>
     */
    private void analyse() {
        String selectedName = (String) analysisTypeComboBox.getSelectedItem();
        int selectedType = AnalysisTypeConstants.NAME_TO_ID.getOrDefault(selectedName, 0);
        mainPanel.showAnalysis(selectedType);
    }
}

// 2 - flights grouped by the operator
// List<Output> : ICAO (representing specific operator) + value: number of flights.
// -> later: take specific aircraft information by ICAO (operator)
// 3 - flights grouped by the model
// List<Output> : ICAO (representing specific model) + value: number of flights.
// -> later: take specific aircraft information by ICAO (model)
// 4 - getting average time for top operators, then plotting it
// List<Output> : ICAO (representing specific operator) + value: avarage time of flight per operator.
// -> later: take specific aircraft information by ICAO (operator)
// 5 - getting percentage of long flights for top operators
// List<Output> : ICAO (representing specific operator) + value: percentage of long flights.
// -> later: take specific aircraft information by ICAO (operator)
