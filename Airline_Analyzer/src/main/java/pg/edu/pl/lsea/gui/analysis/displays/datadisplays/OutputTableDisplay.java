package pg.edu.pl.lsea.gui.analysis.displays.datadisplays;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisDisplay;
import pg.edu.pl.lsea.gui.analysis.utils.AircraftParser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

/**
 * Class representing a display that will show up when analysis happens.
 */
public class OutputTableDisplay extends AnalysisDisplay {
    private int currentPage = 0;
    private final List<Output> outputList;
    private final AircraftParser aircraftParser;

    /**
     * Constructor of the output table display.
     * @param outputList - the list of outputs
     */
    public OutputTableDisplay(List<Output> outputList, AircraftParser aircraftParser) {
        this.outputList = outputList;
        this.aircraftParser = aircraftParser;

        setLayout(new BorderLayout());
        displayPage(currentPage);
    }

    /**
     * Display page with specific number of records.
     * Goes through the aircraft parser, which makes it possible to display aircraft information with output information.
     * Aircraft information: Model, Operator, Owner. The aircraft information might not get found - appropriate information will be given.
     * Output information: icao24, value.
     * @param page int - page number
     */
    private void displayPage(int page) {
        removeAll();

        int start = page * NUMBER_OF_RECORDS_SHOWN;
        int end = Math.min(start + NUMBER_OF_RECORDS_SHOWN, outputList.size());

        JPanel listPanel = new JPanel(new GridLayout(NUMBER_OF_RECORDS_SHOWN, 1));
        for (int i = start; i < end; i++) {
            Output o = outputList.get(i);

            Aircraft aircraft = aircraftParser.getAircraftByIcao(o.getIcao24());
            String text = "ICAO24: " + o.getIcao24() + " | Value: " + o.Value;
            if (aircraft != null) {
                text += " | Model: " + aircraft.getModel()
                        + " | Operator: " + aircraft.getOperator()
                        + " | Owner: " + aircraft.getOwner();
            } else {
                text += " | Aircraft info not found";
            }
            listPanel.add(new JLabel(text));
        }

        add(listPanel, BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Create the panel for navigation with previous, next buttons, and a possibility to choose the page.
     * @return JPanel containing navigation controls.
     */
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();

        JButton prevButton = createPrevButton();
        JButton nextButton = createNextButton();

        JLabel pageInfoLabel = createPageInfoLabel();
        JTextField pageInputField = createPageInputField();
        JButton goToPageButton = createGoToPageButton(pageInputField);

        navPanel.add(prevButton);
        navPanel.add(pageInfoLabel);
        navPanel.add(pageInputField);
        navPanel.add(goToPageButton);
        navPanel.add(nextButton);

        return navPanel;
    }

    /**
     * Creates a "Previous" button - the button which makes it possible to go to the previous page.
     * The button is disabled if already on the first page.
     * @return JButton - previous page button.
     */
    private JButton createPrevButton() {
        JButton prevButton = new JButton("Previous");
        prevButton.setEnabled(currentPage > 0);
        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage(currentPage);
            }
        });
        return prevButton;
    }

    /**
     * Creates a "Next" button - the button which makes it possible to go to the next page.
     * The button is disabled if already on the last page.
     * @return JButton - next page button.
     */
    private JButton createNextButton() {
        JButton nextButton = new JButton("Next");
        nextButton.setEnabled((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < outputList.size());
        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < outputList.size()) {
                currentPage++;
                displayPage(currentPage);
            }
        });
        return nextButton;
    }

    /**
     * Creates a label that displays the current page number and the total number of pages.
     * @return JLabel showing the information about pages.
     */
    private JLabel createPageInfoLabel() {
        int totalPages = (int) Math.ceil((double) outputList.size() / NUMBER_OF_RECORDS_SHOWN);
        return new JLabel("Page " + (currentPage + 1) + " of " + totalPages);
    }

    /**
     * Creates and returns a text field initialized with the current page number.
     * User will input the page number to jump to.
     * @return JTextField initialized with the current page number.
     */
    private JTextField createPageInputField() {
        JTextField pageInputField = new JTextField(3);
        pageInputField.setText(String.valueOf(currentPage + 1));
        return pageInputField;
    }

    /**
     * Creates a "Go" button that allows the user to navigate to a specific page number entered in a text field.
     * Will check if the input is valid.
     * @param pageInputField (JTextField) - the user input for page number.
     * @return JButton configured to jump to the entered page number.
     */
    private JButton createGoToPageButton(JTextField pageInputField) {
        JButton goToPageButton = new JButton("Go");
        goToPageButton.addActionListener(e -> {
            try {
                int totalPages = (int) Math.ceil((double) outputList.size() / NUMBER_OF_RECORDS_SHOWN);
                int pageNumber = Integer.parseInt(pageInputField.getText()) - 1;
                if (pageNumber >= 0 && pageNumber < totalPages) {
                    currentPage = pageNumber;
                    displayPage(currentPage);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid page number.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        });
        return goToPageButton;
    }
}
