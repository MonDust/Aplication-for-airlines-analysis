package pg.edu.pl.lsea.backend.services.analysis;

import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.grouping.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;

/**
 * Base class for analysis classes
 */
public abstract class BaseAnalysis {
    /**
     * handling data
     */
    protected final FlightRepo flightRepo;
    protected final FlightToResponseMapper flightToResponseMapper;

    protected final EnrichedFlightRepo enrichedFlightRepo;
    protected final EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;

    protected final AircraftRepo aircraftRepo;
    protected final AircraftToResponseMapper aircraftToResponseMapper;


    // Analysis tools
    protected final SortingCalculator sortingCalculator = new SortingCalculator();

    protected final PropertiesCalculator propertiesCalculator = new PropertiesCalculator();
    protected final ParallelGroupingTool parallelGroupingTool = new ParallelGroupingTool();


    /**
     * Constructor for BaseAnalysis class.
     * @param flightRepo - repository; h2 database
     * @param flightToResponseMapper - mapper; h2 database
     * @param enrichedFlightRepo - repository; h2 database
     * @param enrichedFlightToResponseMapper - mapper; h2 database
     * @param aircraftRepo - repository; h2 database
     * @param aircraftToResponseMapper - mapper; h2 database
     */
    public BaseAnalysis(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper,
                        EnrichedFlightRepo enrichedFlightRepo, EnrichedFlightToResponseMapper enrichedFlightToResponseMapper,
                        AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper ) {

        // Flights
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        // Enriched Flights
        this.enrichedFlightRepo = enrichedFlightRepo;
        this.enrichedFlightToResponseMapper = enrichedFlightToResponseMapper;

        // Aircrafts
        this.aircraftRepo = aircraftRepo;
        this.aircraftToResponseMapper = aircraftToResponseMapper;
    }


}
