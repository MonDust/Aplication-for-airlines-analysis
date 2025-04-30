package pg.edu.pl.lsea.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;

import pg.edu.pl.lsea.backend.data.analyzer.grouping.GroupingTool;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.grouping.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.data.storage.DataStorage;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;
import pg.edu.pl.lsea.backend.services.analysis.typesofanalysis.GroupedAndForPlotAnalysis;

import java.util.List;

/**
 * Service that handles logic behind requests from controller AnalysisController
 */
@Service
@Transactional
public class AnalysisService {
    /**
     * handling data
     */
    private final FlightRepo flightRepo;
    private final FlightToResponseMapper flightToResponseMapper;
 //   DataStorage dataStorage;

    private final EnrichedFlightRepo enrichedFlightRepo;
    private final EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;



    private final AircraftRepo aircraftRepo;
    private final AircraftToResponseMapper aircraftToResponseMapper;
    /**
     * Inicialization of tools that are nessesary for handling data
     */
    private final SortingCalculator sortingCalculator = new SortingCalculator();
    private final ParallelGroupingTool parallelGroupingTool= new ParallelGroupingTool();
    private final GroupingTool groupingTool= new GroupingTool();
    private final PropertiesCalculator propertiesTool= new PropertiesCalculator();

    /**
     * Constructor for AnalysisService class
     * @param flightRepo - flight repository; h2 database
     * @param flightToResponseMapper - mapper; h2 database
     */
    public AnalysisService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper, EnrichedFlightRepo enrichedFlightRepo,
                           EnrichedFlightToResponseMapper enrichedFlightToResponseMapper, AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper ) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        this.enrichedFlightRepo = enrichedFlightRepo;
        this.enrichedFlightToResponseMapper = enrichedFlightToResponseMapper;


        this.aircraftRepo = aircraftRepo;
        this.aircraftToResponseMapper = aircraftToResponseMapper;


    }

    /**
     * Get all the flights from the flight repository.
     * @return List of FlightResponse
     */
    public List<FlightResponse> getAll() {
        return flightRepo.findAll()
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }


    /**
     * Gives amount of flights per each ICAO
     * @return list with amount of flights per icao24 written in output objects
     */
    public List<Output>  sortByAmountOfFlights() {

        return (this.sortingCalculator.sortByAmountOfFlights(enrichedFlightRepo.findAll()));



    }


    /**
     * This function gives percentage of flights that classify as long per each list in list of lists.
     * The flights are grouped by models.
     * @return list of outputs; value - percentage of flight that classify as long stored in output format
     */
    public List<Output> givePercentageOfLongFlights() {

        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return propertiesTool.givePercentageOfLongFlights(listOfLists_model);
    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> sortByTimeOfFlights() {


        return sortingCalculator.sortByTimeOfFlights(enrichedFlightRepo.findAll());

    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> printAllAverages() {

        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return         propertiesTool.printAllAverages(listOfLists_model);

    }

    /**
     * Calculates average time in air for list of flights
     * @return averred time per inputed flights
     */
    public int calculateAverageTimeInAir() {

        return propertiesTool.calculateAverageTimeInAir(enrichedFlightRepo.findAll());
    }


    /**
     * Returns list of list which is containing any long flights
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {



        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return groupingTool.findLongFlightsForEachModel(listOfLists_model);
    }

    public List<List<EnrichedFlight>> giveTopNOperators(int HowMuchOperators) {

        List<List<EnrichedFlight>> listOfLists_operator = parallelGroupingTool.groupFlightsByOperator(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return sortingCalculator.giveTopNOperators(listOfLists_operator, HowMuchOperators);
    }

}
