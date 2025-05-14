package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
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

    @GetMapping("/getTopNOperators/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNOperators(@PathVariable int howMuchOperators) {
        return analysisService.getTopNOperatorWithNumberOfFlights(howMuchOperators);
    }

    @GetMapping("/getTopNModels/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNModels(@PathVariable int howMuchOperators) {
        return analysisService.getTopNModelWithNumberOfFlights(howMuchOperators);
    }

    @GetMapping("/getTopNPercentageOfLongFlights/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNPercentageOfLongFlights(@PathVariable int howMuchOperators) {
        return analysisService.getTopNPercentageOfLongFlights_GroupedByOperator(howMuchOperators);
    }

    @GetMapping("/getTopNAverageTime/{howMuchOperators}")
    @ResponseStatus(HttpStatus.OK)
    public List<Output> getTopNAverageTime(@PathVariable int howMuchOperators) {
        return analysisService.getTopNAverageTime_GroupedByOperator(howMuchOperators);
    }

}
