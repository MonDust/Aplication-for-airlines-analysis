package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.gui.maincomponents.MainPanel;
import pg.edu.pl.lsea.utils.guispecific.optionmapping.InformationTypeConstants;

import javax.swing.*;

/**
 * Choosing Information Window that allows the user to select the type of information to display in the GUI.
 * The user chooses from predefined information categories.
 */
public class ChoosingInformationWindow extends BaseChoosingWindow{
    final private JComboBox<String> informationComboBox;

    /**
     * Constructor for ChoosingInformationWindow.
     * Creates a dropdown for selecting information type and a button to confirm the selection.
     * @param panel - the main panel of the application GUI.
     */
    public ChoosingInformationWindow(MainPanel panel) {
        super(panel);
        setTitle("Choose Information to show");

        // Select information to show box
        JLabel informationLabel = new JLabel("Select information to show:");
        informationComboBox = new JComboBox<>(InformationTypeConstants.INFO_NAME_TO_ID.keySet().toArray(new String[0]));
        add(informationLabel);
        add(informationComboBox);

        // Show Information Button
        JButton loadButton = new JButton("Show Information");
        loadButton.addActionListener(e -> setInformation());
        add(loadButton);

        // Set the window to be visible when this frame is shown
        setVisible(true);
    }

    /**
     * Retrieves the selected information type and then uses show information function (the one to display information in GUI).
     */
    public void setInformation() {
        String selectedName = (String) informationComboBox.getSelectedItem();
        int selectedType = InformationTypeConstants.INFO_NAME_TO_ID.getOrDefault(selectedName, 0);
        mainPanel.showInformation(selectedType);
    }
}
