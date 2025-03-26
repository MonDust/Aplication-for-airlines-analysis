package pg.edu.pl.lsea.gui.analysis;

import pg.edu.pl.lsea.data.analyzer.SortingCaluclator;
import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Trackable;

import javax.sound.midi.Track;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_RECORDS_SHOWN;

public class AnalysisDisplay extends AnalysisArea {
    final private List<Flight> flightData;
    final private List<Aircraft> aircraftData;
    final private NullRemover nullRemover = new NullRemover();
    final private DataEnrichment dataEnrichment = new DataEnrichment();
    final private SortingCaluclator sortingCaluclator = new SortingCaluclator();

    /**
     * Add data to AnalysisArea
     * @param flightData - the to be added flight data
     * @param aircraftData - the to be added aircraft data
     */
    public AnalysisDisplay (List<Flight> flightData, List<Aircraft> aircraftData) {
        this.flightData = flightData;
        this.aircraftData = aircraftData;
    }

    private void showData(List<Flight> flightData, List<Aircraft> aircraftData) {
        // nullRemover.TransformAll(aircraftData, flightData);
        // List<EnrichedFlight> enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flightData);
        // sortingCaluclator.analyzeDataForDashbord(aircraftData, enrichedFlights);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        showData(flightData, aircraftData);
    }
}
