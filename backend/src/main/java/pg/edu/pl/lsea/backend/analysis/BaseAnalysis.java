package pg.edu.pl.lsea.backend.analysis;

import pg.edu.pl.lsea.backend.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.backend.data.engieniering.NullRemover;
import pg.edu.pl.lsea.backend.data.storage.DataStorage;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Flight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.List;

/**
 * Base class for analysis classes
 */
public abstract class BaseAnalysis {
    protected final DataStorage storage = DataStorage.getInstance();

    protected final SortingCalculator sortingCalculator = new SortingCalculator();
    protected final GroupingTool groupingTool = new GroupingTool();

    protected final PropertiesCalculator propertiesCalculator = new PropertiesCalculator();
    protected final ParallelGroupingTool parallelGroupingTool = new ParallelGroupingTool();

    /**
     * Constructor for the class.
     */
    public BaseAnalysis() {}

    /**
     * Function preparing flights.
     * Removing the records with missing data and enriching the list of flights.
     * @return list of enriched flights
     */
    public List<EnrichedFlight> prepareFlights() {
        NullRemover nullRemover = new NullRemover();
        DataEnrichment enricher = new DataEnrichment();

        List<Flight> flights = storage.getFlights();
        nullRemover.TransformFlights(flights);

        return enricher.CreateEnrichedListOfFlights(flights);
    }

    /**
     * Function preparing aircrafts.
     * Removing the records with missing data.
     * @return list of aircrafts
     */
    public List<Aircraft> prepareAircrafts() {
        NullRemover nullRemover = new NullRemover();

        List<Aircraft> aircrafts = storage.getAircrafts();
        nullRemover.TransformAircrafts(aircrafts);

        return aircrafts;
    }

    /**
     * Sorting flights by the time of flight
     * @return list of outputs
     */
    public List<Output> getListSortedByTimeOfFlights() {
        SortingCalculator sortCalc = new SortingCalculator();
        return sortCalc.sortByTimeOfFlights(prepareFlights());
    }

    /**
     * Sorting flights by the number of flights
     * @return list of outputs
     */
    public List<Output> getListSortedByNumberOfFlights() {
        SortingCalculator sortCalc = new SortingCalculator();
        return sortCalc.sortByAmountOfFlights(prepareFlights());
    }

}
