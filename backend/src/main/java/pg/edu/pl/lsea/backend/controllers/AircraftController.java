package pg.edu.pl.lsea.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.services.AircraftService;

import java.util.List;

/**
 * Class responsible for handling API endpoints (part of MVC architecture)
 */
@RestController
@RequestMapping("/aircrafts")
public class AircraftController {
    /**
     * aircraftService is used for all logic
     */
    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AircraftResponse> getAircrafts() {
        return aircraftService.getAll();
    };

    @GetMapping("/{icao24}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftResponse getAircraft(@PathVariable("icao24") String icao24) {
        return aircraftService.getByIcao(icao24);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AircraftResponse createAircraft(@RequestBody AircraftResponse request) {
        return aircraftService.create(request);
    }


    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AircraftResponse> createAircrafts(@RequestBody List<AircraftResponse> request) {
        return aircraftService.createBulk(request);
    }


    @PutMapping("/{icao24}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftResponse updateAircraft(@PathVariable String icao24, @RequestBody AircraftResponse request) {
        return aircraftService.updateAircraft(icao24, request);
    }

    @PatchMapping("/{icao24}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftResponse patchAircraft(@PathVariable String icao24, @RequestBody AircraftResponse request) {
        return aircraftService.patchAircraft(icao24, request);
    }

    @DeleteMapping("/{icao24}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAircraft(@PathVariable String icao24) {
        aircraftService.deleteAircraft(icao24);
    }
}
