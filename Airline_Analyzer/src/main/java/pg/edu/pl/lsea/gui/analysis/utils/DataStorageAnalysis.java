package pg.edu.pl.lsea.gui.analysis.utils;

import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;

import java.util.List;

/**
 * Class responsible for preparing data
 */
public class DataStorageAnalysis {

    /**
     * Constructor for the class.
     */
    public DataStorageAnalysis() {}

    /**
     * Function preparing flights.
     * Removing the records with missing data and enriching the list of flights.
     * @return list of enriched flights
     */
    public static List<EnrichedFlight> prepareFlights() {
        NullRemover nullRemover = new NullRemover();
        DataEnrichment enricher = new DataEnrichment();
        List<Flight> flights = DataStorage.getInstance().getFlights();
        nullRemover.TransformFlights(flights);
        List<EnrichedFlight> enrichedFlights = enricher.CreateEnrichedListOfFlights(flights);
        return enrichedFlights;
    }

    /**
     * Function preparing aircrafts.
     * Removing the records with missing data.
     * @return list of aircrafts
     */
    public static List<Aircraft> prepareAircrafts() {
        NullRemover nullRemover = new NullRemover();
        List<Aircraft> aircrafts = DataStorage.getInstance().getAircrafts();
        nullRemover.TransformAircrafts(aircrafts);
        return aircrafts;
    }
}
