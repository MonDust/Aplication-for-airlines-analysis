package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.services.AnalysisService;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;


    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/sortByAmountOfFlights")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> sortByAmountOfFlights() {

        return analysisService.sortByAmountOfFlights();
    }

    @GetMapping("/sortByTimeOfFlights")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> sortByTimeOfFlights() {

        return analysisService.sortByTimeOfFlights();
    }

    @GetMapping("/giveTopNOperators/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<List<EnrichedFlight>> printAircraftList(@PathVariable int howMuchOperators) {
        return analysisService.giveTopNOperators(howMuchOperators);
    }

    @GetMapping("/givePercentageOfLongFlights")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> givePercentageOfLongFlights() {

        return analysisService.givePercentageOfLongFlights();
    }

    @GetMapping("/printAllAverages")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> printAllAverages() {

        return analysisService.printAllAverages();
    }

    @GetMapping("/calculateAverageTimeInAir")
    @ResponseStatus(HttpStatus.OK)
    public int calculateAverageTimeInAir() {

        return analysisService.calculateAverageTimeInAir();
    }

    @GetMapping("/findLongFlightsForEachModel")
    @ResponseStatus(HttpStatus.OK)
    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {

        return analysisService.findLongFlightsForEachModel();
    }


    /*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(@RequestBody FlightResponse request) {
        return analysisService.create(request);
    }


    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<FlightResponse> createFlights(@RequestBody List<FlightResponse> request) {
        return analysisService.createBulk(request);
    }*/


    public static void performAnalysis(int threads, List<EnrichedFlight> enrichedFlights, List<Aircraft> aircrafts) {

        // Sort the data
        SortingCalculator sortingCalculator = new SortingCalculator();


        // Firstly, group aircraft by models and store a list of flights for each model
        ParallelGroupingTool parallelGroupingTool = new ParallelGroupingTool();
        // This method runs with multiple threads internally:
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, aircrafts, threads);
        List<List<EnrichedFlight>> listOfLists_operator = parallelGroupingTool.groupFlightsByOperator(enrichedFlights, aircrafts, threads);


        // Analysis
        GroupingTool groupingTool = new GroupingTool();
        List<List<EnrichedFlight>> longFlights = groupingTool.findLongFlightsForEachModel(listOfLists_model);

        // Secondly, for each model group, print average time in the air of all flights
        PropertiesCalculator propertiesTool = new PropertiesCalculator();
        propertiesTool.printAllAverages(listOfLists_model);
        propertiesTool.givePercentageOfLongFlights(listOfLists_model);


        sortingCalculator.giveTopNOperators(listOfLists_operator, 10);
        sortingCalculator.sortByAmountOfFlights(enrichedFlights);
        sortingCalculator.sortByTimeOfFlights(enrichedFlights);
    }
}
