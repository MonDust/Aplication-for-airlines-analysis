package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.services.AnalysisService;

import java.util.List;

/**
 * Controller that handles endpoints for analysis
 */
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

    @GetMapping("/givePercentageOfLongFlights")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> givePercentageOfLongFlights() {

        return analysisService.givePercentageOfLongFlights_ModelGrouping();
    }

    @GetMapping("/printAllAverages")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> printAllAverages() {

        return analysisService.giveAllAverages_groupedByModel();
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

    // needed for frontend

    @GetMapping("/giveTopNOperators")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> giveTopNOperators() {
        return analysisService.getTopNOperatorWithNumberOfFlights();
    }

    @GetMapping("/giveTopNOperators/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> giveTopNOperators(@PathVariable int howMuchOperators) {
        return analysisService.getTopNOperatorWithNumberOfFlights(howMuchOperators);
    }

    @GetMapping("/giveTopNModels")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> giveTopNModels() {
        return analysisService.getTopNModelWithNumberOfFlights();
    }

    @GetMapping("/giveTopNModels/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> giveTopNModels(@PathVariable int howMuchOperators) {
        return analysisService.getTopNModelWithNumberOfFlights(howMuchOperators);
    }

    @GetMapping("/getTopNPercentageOfLongFlights")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNPercentageOfLongFlights() {
        return analysisService.getTopNPercentageOfLongFlights_GroupedByOperator();
    }

    @GetMapping("/getTopNPercentageOfLongFlights/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNPercentageOfLongFlights(@PathVariable int howMuchOperators) {
        return analysisService.getTopNPercentageOfLongFlights_GroupedByOperator(howMuchOperators);
    }

    @GetMapping("/getTopNAverageTime")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNAverageTime() {
        return analysisService.getTopNAverageTime_GroupedByOperator();
    }

    @GetMapping("/getTopNAverageTime/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNAverageTime(@PathVariable int howMuchOperators) {
        return analysisService.getTopNAverageTime_GroupedByOperator(howMuchOperators);
    }

}
