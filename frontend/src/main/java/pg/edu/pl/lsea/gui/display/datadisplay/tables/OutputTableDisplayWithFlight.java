package pg.edu.pl.lsea.gui.display.datadisplay.tables;

import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Output;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

/**
 * Displays a list of Output records along with associated Flight information in a vertical table format (1 column).
 */
public class OutputTableDisplayWithFlight extends TableDisplay<Output> {
    /**
     * Constructor for OutputTableDisplayWithFlight.
     * @param outputs A list of Output objects to be displayed.
     */
    public OutputTableDisplayWithFlight(List<Output> outputs) {
        // list of outputs gotten: API
        super(outputs);
    }

    /**
     * Creates a panel displaying a subset of the output records, from index start to end.
     * Each record is shown as a JLabel, formatted with Output and Flight details.
     * @param start - starting index (inclusive).
     * @param end - ending index (exclusive).
     * @return JPanel containing the list of formatted output records.
     */
    @Override
    protected JPanel getListPanel(int start, int end) {
        JPanel listPanel = new JPanel(new GridLayout(NUMBER_OF_RECORDS_SHOWN, 1));
        for (int i = start; i < end; i++) {
            Output o = itemList.get(i);

            String text = getTextOutput(o);
            listPanel.add(new JLabel(text));
        }
        return listPanel;
    }

    /**
     * Formats a single Output record into a string that includes its value and any matching Flight information.
     * @param o Output object whose data is to be formatted.
     * @return A string with ICAO24, value, and Flight data (if available).
     */
    private String getTextOutput(Output o) {
        // get Aircraft by icao o.getIcao
        Flight flight = new Flight();
        String text = "ICAO24: " + o.getIcao24() + " | Value: " + o.getValue();
        if (flight != null) {
            text += " | Arrival: " + flight.getArrivalAirport()
                    + " | Departure: " + flight.getDepartureAirport()
                    + " | First Seen: " + flight.getFirstSeen()
                    + " | Last Seen: " + flight.getLastSeen();
        } else {
            text += " | Flight info not found";
        }
        return text;
    }
}
