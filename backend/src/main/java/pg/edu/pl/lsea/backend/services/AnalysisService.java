package pg.edu.pl.lsea.backend.services;

import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;

import pg.edu.pl.lsea.backend.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.backend.data.storage.DataStorage;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Flight;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.List;

/**
 * Service that handles logic behind requests from controller AnalysisController
 */
@Service
public class AnalysisService {


    /**
     * handling data
     */
    private final FlightRepo flightRepo;
    private final FlightToResponseMapper flightToResponseMapper;
    DataStorage dataStorage;

    /**
     * Inicialization of tools that are nessesary for handling data
     */
    private final SortingCalculator sortingCalculator = new SortingCalculator();
    private final ParallelGroupingTool parallelGroupingTool= new ParallelGroupingTool();
    private final GroupingTool groupingTool= new GroupingTool();
    private final PropertiesCalculator propertiesTool= new PropertiesCalculator();

    /**
     * Constructor for AnalysisService class
     * @param flightRepo h2 database
     * @param flightToResponseMapper h2 database
     */
    public AnalysisService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        this.dataStorage = DataStorage.getInstance();

    }

    public List<FlightResponse> getAll() {
        return flightRepo.findAll()
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }


    /**
     * Gives amount of flights per each ICAO
     * @return amount of flights per model writen in outpu objects
     */
    public List<Output>  sortByAmountOfFlights() {

        return (this.sortingCalculator.sortByAmountOfFlights(dataStorage.getEnrichedFlights()));



    }


    /**
     * this function gives percentage of flights that classify as long per each list in list of lists
     * @return percentage of flight that classify as long stored in output format
     */
    public List<Output> givePercentageOfLongFlights() {

        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(dataStorage.getEnrichedFlights(), dataStorage.getAircrafts(), 8);
        return propertiesTool.givePercentageOfLongFlights(listOfLists_model);
    }


    /**
     * Gives amount of time in air per each ICAO
     * @return in output object ICAO and it's time in air
     */
    public List<Output> sortByTimeOfFlights() {


        return sortingCalculator.sortByTimeOfFlights(dataStorage.getEnrichedFlights());

    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> printAllAverages() {

        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(dataStorage.getEnrichedFlights(), dataStorage.getAircrafts(), 8);
        return         propertiesTool.printAllAverages(listOfLists_model);

    }

    /**
     * Calculates average time in air for list of flights
     * @return averred time per inputed flights
     */
    public int calculateAverageTimeInAir() {

        return propertiesTool.calculateAverageTimeInAir(dataStorage.getEnrichedFlights());
    }


    /**
     * returns list of list which is containing any long flights
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {



        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(dataStorage.getEnrichedFlights(), dataStorage.getAircrafts(), 8);
        return groupingTool.findLongFlightsForEachModel(listOfLists_model);
    }

    public List<List<EnrichedFlight>> giveTopNOperators(int HowMuchOperators) {

        List<List<EnrichedFlight>> listOfLists_operator = parallelGroupingTool.groupFlightsByOperator(dataStorage.getEnrichedFlights(), dataStorage.getAircrafts(), 8);
        return sortingCalculator.giveTopNOperators(listOfLists_operator, HowMuchOperators);
    }
}
