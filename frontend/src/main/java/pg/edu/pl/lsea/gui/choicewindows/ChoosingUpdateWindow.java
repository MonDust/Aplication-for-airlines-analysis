package pg.edu.pl.lsea.gui.choicewindows;

import pg.edu.pl.lsea.gui.maincomponents.MainPanel;
import pg.edu.pl.lsea.utils.guispecific.optionmapping.UpdateTypeConstants;

import javax.swing.*;

/**
 * Choosing Update Window that allows the user to select the type of information to delete/update.
 * The user chooses from predefined categories.
 */
public class ChoosingUpdateWindow extends BaseChoosingWindow {
    final private JComboBox<String> updateComboBox;
    /**
     * Constructor for ChoosingUpdateWindow.
     * Creates a dropdown for selecting information type and a button to confirm the selection.
     * @param panel - the main panel of the application GUI.
     */
    public ChoosingUpdateWindow(MainPanel panel) {
        super(panel);
        setTitle("Choose update/deletion");

        // Select information to show box
        JLabel updateLabel = new JLabel("Select update/deletion:");
        updateComboBox = new JComboBox<>(UpdateTypeConstants.UPDATE_TO_ID.keySet().toArray(new String[0]));
        add(updateLabel);
        add(updateComboBox);

        // Show Update Button
        JButton updateButton = new JButton("Select");
        updateButton.addActionListener(e -> makeUpdate());
        add(updateButton);

        // Set the window to be visible when this frame is shown
        setVisible(true);
    }

    /**
     * Make a selected update.
     */
    public void makeUpdate() {
        String selectedName = (String) updateComboBox.getSelectedItem();
        int selectedType = UpdateTypeConstants.UPDATE_TO_ID.getOrDefault(selectedName, 0);
        mainPanel.makeUpdate(selectedType);
    }
}
