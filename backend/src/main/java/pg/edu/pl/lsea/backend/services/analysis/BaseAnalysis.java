package pg.edu.pl.lsea.backend.services.analysis;

import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.controllers.dto.EnrichedFlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.grouping.GroupingTool;
import pg.edu.pl.lsea.backend.data.analyzer.grouping.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;

import java.util.List;

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
    protected final GroupingTool groupingTool = new GroupingTool();

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


    // GET ALL //


    /**
     * Get all the flights from the flight repository.
     * @return List of FlightResponse
     */
    public List<FlightResponse> getAllFlights() {
        return flightRepo.findAll()
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }

    /**
     * Get all the aircrafts from the flight repository.
     * @return List of AircraftResponse
     */
    public List<AircraftResponse> getAllAircrafts() {
        return aircraftRepo.findAll()
                .stream()
                .map(aircraftToResponseMapper)
                .toList();
    }

    /**
     * Get all the enrichedFlights from the flight repository.
     * @return List of EnrichedFlightResponse
     */
    public List<EnrichedFlightResponse> getAllEnrichedFlights() {
        return enrichedFlightRepo.findAll()
                .stream()
                .map(enrichedFlightToResponseMapper)
                .toList();
    }
}
