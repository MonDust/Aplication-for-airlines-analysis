package pg.edu.pl.lsea.gui.analysis.displays.datadisplays;

import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisArea;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

/**
 * Class representing a display that will show up when analysis happens.
 */
public class OutputTableDisplay extends AnalysisArea {
    private int currentPage = 0;
    private final List<Output> outputList;

    public OutputTableDisplay(List<Output> outputList) {
        super();
        this.outputList = outputList;
        setLayout(new BorderLayout());
        displayPage(currentPage);
    }

    private void displayPage(int page) {
        removeAll();

        int start = page * NUMBER_OF_RECORDS_SHOWN;
        int end = Math.min(start + NUMBER_OF_RECORDS_SHOWN, outputList.size());

        JPanel listPanel = new JPanel(new GridLayout(NUMBER_OF_RECORDS_SHOWN, 1));
        for (int i = start; i < end; i++) {
            Output o = outputList.get(i);
            JLabel label = new JLabel("ICAO24: " + o.getIcao24() + "  |  Value: " + o.Value);
            listPanel.add(label);
        }

        add(listPanel, BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();

        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        prevButton.setEnabled(currentPage > 0);
        nextButton.setEnabled((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < outputList.size());

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage(currentPage);
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < outputList.size()) {
                currentPage++;
                displayPage(currentPage);
            }
        });

        // Display total pages and add a field for page input
        int totalPages = (int) Math.ceil((double) outputList.size() / NUMBER_OF_RECORDS_SHOWN);
        JLabel pageInfoLabel = new JLabel("Page " + (currentPage + 1) + " of " + totalPages);

        JTextField pageInputField = new JTextField(3);
        pageInputField.setText(String.valueOf(currentPage + 1));

        JButton goToPageButton = new JButton("Go");
        goToPageButton.addActionListener(e -> {
            try {
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

        navPanel.add(prevButton);
        navPanel.add(pageInfoLabel);
        navPanel.add(pageInputField);
        navPanel.add(goToPageButton);
        navPanel.add(nextButton);

        return navPanel;
    }
}
