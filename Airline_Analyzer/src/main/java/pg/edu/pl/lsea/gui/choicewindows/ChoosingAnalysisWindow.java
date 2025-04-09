package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.utils.AnalysisTypeConstants;
import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;

/**
 * Choosing Analysis Window which lets choose analysis type to perform
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
     * Analyse the data
     */
    private void analyse() {
        String selectedName = (String) analysisTypeComboBox.getSelectedItem();
        int selectedType = AnalysisTypeConstants.NAME_TO_ID.getOrDefault(selectedName, 0);
        mainPanel.performAnalysis(selectedType);
    }
}
