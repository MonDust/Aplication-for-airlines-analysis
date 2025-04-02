package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.gui.maincomponents.MainPanel;

import javax.swing.*;

/**
 * Choosing Analysis Window which lets choose analysis type to perform
 */
public class ChoosingAnalysisWindow extends BaseChoosingWindow{
    final private JComboBox<String> analysisTypeComboBox;

    /**
     * Constructor for ChoosingAnalysisWindow class
     * @param panel - main panel
     */
    public ChoosingAnalysisWindow(MainPanel panel) {
        super(panel);
        setTitle("Choose Analysis Options");

        // Analysis Type Selection
        JLabel analysisTypeLabel = new JLabel("Select analysis type:");
        // Available File Types
        String[] analysisTypes = new String[]{"Analysis 1", "Analysis 2"};
        analysisTypeComboBox = new JComboBox<>(analysisTypes);
        add(analysisTypeLabel);
        add(analysisTypeComboBox);

        // Start analysis button
        JButton loadButton = new JButton("Analyze");
        loadButton.addActionListener(e -> analyse());
        add(loadButton);

        // Set the window to be visible when this frame is shown
        setVisible(true);
    }

    private int getAnalysisType(){
        String analysisType = (String) analysisTypeComboBox.getSelectedItem();
        switch(analysisType){
            case "Analysis 1":
                return 1;
            case "Analysis 2":
                return 2;
            default:
                return 0;
        }
    }

    /**
     * Analyse the data
     */
    private void analyse() {
        mainPanel.performAnalysis(getAnalysisType());
    }


}
