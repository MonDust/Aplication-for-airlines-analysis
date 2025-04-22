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

@Service
public class AnalysisService {
    private final FlightRepo flightRepo;
    private final FlightToResponseMapper flightToResponseMapper;
    DataStorage dataStorage;
    private final SortingCalculator sortingCalculator = new SortingCalculator();
    private final ParallelGroupingTool parallelGroupingTool= new ParallelGroupingTool();
    private final GroupingTool groupingTool= new GroupingTool();
    private final PropertiesCalculator propertiesTool= new PropertiesCalculator();
    private List<List<EnrichedFlight>> listOfLists_model;
    private List <EnrichedFlight> enrichedFlights;

    private DataEnrichment enrichmentTool = new DataEnrichment();


    public AnalysisService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        this.dataStorage = DataStorage.getInstance();
//        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());



    }

    public List<FlightResponse> getAll() {
        return flightRepo.findAll()
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }

    public FlightResponse getByIcao(String icao) {
        return flightRepo.findById(icao)
                .map(flightToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "icao24", icao));
    }

    public FlightResponse create(FlightResponse request) {
        Flight flight = new Flight(
                request.icao24(),
                request.firstSeen(),
                request.lastSeen(),
                request.departureAirport(),
                request.arrivalAirport());

        flightRepo.save(flight);
        return flightToResponseMapper.apply(flight);
    }

    public List<FlightResponse> createBulk(List<FlightResponse> request) {
        List<Flight> flights = request.stream()
                .map(r -> new Flight(
                        r.icao24(),
                        r.firstSeen(),
                        r.lastSeen(),
                        r.departureAirport(),
                        r.arrivalAirport()))
                .toList();

        flightRepo.saveAll(flights);

        return flights.stream()
                .map(flightToResponseMapper)
                .toList();
    }

    public List<Output>  sortByAmountOfFlights() {
        dataStorage.getFlights();

        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());

        return (this.sortingCalculator.sortByAmountOfFlights(enrichedFlights));



    }

    public List<Output> givePercentageOfLongFlights() {

        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, dataStorage.getAircrafts(), 8);

        return propertiesTool.givePercentageOfLongFlights(listOfLists_model);
    }

    public List<Output> sortByTimeOfFlights() {

        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());


        return sortingCalculator.sortByTimeOfFlights(enrichedFlights);

    }

    public List<Output> printAllAverages() {

        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, dataStorage.getAircrafts(), 8);

        return         propertiesTool.printAllAverages(listOfLists_model);

    }

    public int calculateAverageTimeInAir() {

        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());
        return propertiesTool.calculateAverageTimeInAir(enrichedFlights);
    }



    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {

        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, dataStorage.getAircrafts(), 8);

        return groupingTool.findLongFlightsForEachModel(listOfLists_model);
    }

    public List<List<EnrichedFlight>> giveTopNOperators(int HowMuchOperators) {


        enrichedFlights = enrichmentTool.CreateEnrichedListOfFlights(dataStorage.getFlights());

        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, dataStorage.getAircrafts(), 8);

        return sortingCalculator.giveTopNOperators(listOfLists_model, HowMuchOperators);
    }
}
