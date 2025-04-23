package pg.edu.pl.lsea.gui.display.datadisplay.tables;

import pg.edu.pl.lsea.gui.display.BaseAnalysisDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

// COMMENT:
// TODO - this part should be totally removed or simplified - it makes no sense to display the paginated table with all of the data we have in a database
// If we really want to keep it, at least make these results grouped in some way


/**
 * A generic display component that can display a list of items in a tabular (vertical list) format.
 * Each page displays up to NUMBER_OF_RECORDS_SHOWN items, with navigation controls
 * to switch between pages or jump to a specific one.
 *
 * Override: getListPanel(int, int) to customize how each item is rendered.
 *
 * @param <T> The type of items to be displayed in the table.
 */
public class TableDisplay<T> extends BaseAnalysisDisplay {
    private int currentPage = 0;
    protected List<T> itemList;

    /**
     * Constructor for the generic table display.
     * @param itemList List of items to display.
     */
    public TableDisplay(List<T> itemList) {
        this.itemList = itemList;
        setLayout(new BorderLayout());
        displayPage(currentPage);
    }

    /**
     * Default constructor.
     * Use addList(List) to populate items later.
     */
    public TableDisplay() {
        setLayout(new BorderLayout());
        displayPage(currentPage);
    }

    /**
     * Adds or replaces the list of items to be displayed.
     * @param itemList New list of items to display.
     */
    public void addList(List<T> itemList) {
        this.itemList = itemList;
    }

    /**
     * Display a page of data (specified page of items).
     * @param page The page index to display.
     */
    private void displayPage(int page) {
        removeAll();

        int start = page * NUMBER_OF_RECORDS_SHOWN;
        int end = Math.min(start + NUMBER_OF_RECORDS_SHOWN, itemList.size());

        JPanel listPanel = getListPanel(start, end);

        add(listPanel, BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Constructs the panel displaying the items for the current page.
     *
     * This method can be overridden to customize item rendering.
     *
     * @param start - index of the first item (inclusive).
     * @param end - index of the last item (exclusive).
     * @return JPanel containing item labels.
     */
    protected JPanel getListPanel(int start, int end) {
        JPanel listPanel = new JPanel(new GridLayout(NUMBER_OF_RECORDS_SHOWN, 1));
        for (int i = start; i < end; i++) {
            T item = itemList.get(i);

            // Can implement this otherwise:
            String text = item.toString();
            listPanel.add(new JLabel(text));
        }
        return listPanel;
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
        nextButton.setEnabled((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < itemList.size());
        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < itemList.size()) {
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
        int totalPages = (int) Math.ceil((double) itemList.size() / NUMBER_OF_RECORDS_SHOWN);
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
                int totalPages = (int) Math.ceil((double) itemList.size() / NUMBER_OF_RECORDS_SHOWN);
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
