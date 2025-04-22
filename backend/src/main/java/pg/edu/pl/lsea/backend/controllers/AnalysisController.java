package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.services.AnalysisService;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;


    public AnalysisController (AnalysisService analysisService) { this.analysisService = analysisService;}

    @GetMapping("/sortByAmountOfFlights")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse getFlight(@PathVariable EnrichedFlight enrichedFlights) {
        return analysisService.sortByAmountOfFlights(enrichedFlights);
    };


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(@RequestBody FlightResponse request) {
        return analysisService.create(request);
    }


    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<FlightResponse> createFlights(@RequestBody List<FlightResponse> request) {
        return analysisService.createBulk(request);
    }
}