package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightUpdateRequest;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse getFlight(@PathVariable("id") Long id) {
        return flightService.getById(id);
    }

    // Find all flights for a given icao24
    @GetMapping("/icao24/{icao24}")
    @ResponseStatus(HttpStatus.OK)
    public List<FlightResponse> getFlightsByIcao(@PathVariable String icao24) {
        return flightService.getByIcao(icao24);
    }


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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse updateFlight(@PathVariable("id") Long id, @RequestBody FlightResponse request) {
        return flightService.update(id, request);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse patchFlight(@PathVariable("id") Long id, @RequestBody FlightUpdateRequest request) {
        return flightService.patch(id, request);
    }

    @PatchMapping("/{icao24}/{firstSeen}")
    @ResponseStatus(HttpStatus.OK)
    public FlightResponse patchFlightByIcaoAndFirstSeen(
            @PathVariable String icao24,
            @PathVariable int firstSeen,
            @RequestBody FlightUpdateRequest request) {
        System.out.println(icao24 + " " + firstSeen);
        System.out.println(request.toString());
        return flightService.patchByIcao24AndFirstSeen(icao24, firstSeen, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlight(@PathVariable("id") Long id) {
        flightService.delete(id);
    }

    // This is used in this way:
    // DELETE /api/flights?start=...&end=...
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByTimeRange(@RequestParam("start_date") int startDate, @RequestParam("end_date") int endDate) {
        flightService.deleteByTimeRange(startDate, endDate);
    }
}
