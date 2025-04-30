package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.services.FlightService;

import java.util.List;

/**
 * Class responsible for handling API endpoints (part of MVC architecture)
 */
@RestController
@RequestMapping("/flights")
public class FlightController {
    /**
     * flightService is used for all logic
     */
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FlightResponse> getFlights() {
        return flightService.getAll();
    };

    @GetMapping("/{icao24}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse getFlight(@PathVariable("icao24") String icao24) {
        return flightService.getByIcao(icao24);
    };


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse createFlight(@RequestBody FlightResponse request) {
        return flightService.create(request);
    }


    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<FlightResponse> createFlights(@RequestBody List<FlightResponse> request) {
        return flightService.createBulk(request);
    }
}
