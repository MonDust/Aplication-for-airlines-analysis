package pg.edu.pl.lsea.gui.display.datadisplay.tables;

import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Output;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

public class OutputTableDisplayWithFlight extends TableDisplay<Output> {
    public OutputTableDisplayWithFlight(List<Output> outputs) {
        super(outputs);
    }

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
