package pg.edu.pl.lsea.gui.analysis.displays.datadisplays;

import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisArea;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class EnrichedFlightDisplay extends AnalysisArea {
    private final List<EnrichedFlight> flightList;
    private int currentPage = 0;

    public EnrichedFlightDisplay(List<EnrichedFlight> flightList) {
        this.flightList = flightList;
        setLayout(new BorderLayout());
        displayPage(currentPage);
    }

    private void displayPage(int page) {
        removeAll();

        int start = page * NUMBER_OF_RECORDS_SHOWN;
        int end = Math.min(start + NUMBER_OF_RECORDS_SHOWN, flightList.size());

        JPanel listPanel = new JPanel(new GridLayout(NUMBER_OF_RECORDS_SHOWN, 1));
        for (int i = start; i < end; i++) {
            EnrichedFlight ef = flightList.get(i);
            JLabel label = new JLabel(ef.toString() + ", timeInAir=" + ef.getTimeInAir() + "s");
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
        nextButton.setEnabled((currentPage + 1) * NUMBER_OF_RECORDS_SHOWN < flightList.size());

        prevButton.addActionListener(e -> {
            currentPage--;
            displayPage(currentPage);
        });

        nextButton.addActionListener(e -> {
            currentPage++;
            displayPage(currentPage);
        });

        navPanel.add(prevButton);
        navPanel.add(new JLabel("Page " + (currentPage + 1)));
        navPanel.add(nextButton);

        return navPanel;
    }
}
