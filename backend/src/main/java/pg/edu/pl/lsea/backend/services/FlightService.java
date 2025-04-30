package pg.edu.pl.lsea.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightUpdateRequest;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.backend.data.engieniering.NullRemover;
import pg.edu.pl.lsea.backend.data.storage.DataStorage;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Flight;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;
import java.util.ArrayList;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for flights REST API business logic (part of MVC architecture)
 * It gets and saves data to/from FlightRepo.
 */
@Service
@Transactional
public class FlightService {
    private final FlightRepo flightRepo;
    private final FlightToResponseMapper flightToResponseMapper;
    private final NullRemover nullRemover = new NullRemover();


    /**
     * Constructor for the class.
     * @param flightRepo - flight repository.
     * @param flightToResponseMapper - mapper
     */
    public FlightService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

    }

    /**
     * Returns the list of all flights stored in database (used for GET request)
     * @return List of FlightResponse (FlightResponse = DTO) is what should be exposed via REST API endpoint
     */
    public List<FlightResponse> getAll() {
        return flightRepo.findAll()
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }

    /**
     * Returns one particular flight stored in database (used for GET request)
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint
     */
    public List<FlightResponse> getByIcao(String icao24) {
        return flightRepo.findAllByIcao24(icao24)
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }


    /**
     * Returns one particular flight stored in database (used for GET request) based on ID
     * @param id id of the Flight
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint
     */
    public FlightResponse getById(Long id) {
        return flightRepo.findById(id)
                .map(flightToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "id", id));
    }

    /**
     * Creates a particular flight and stores it in the database (used for POST request).
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public FlightResponse create(FlightResponse request) {
        Flight flight = new Flight(
                request.icao24(),
                request.firstSeen(),
                request.lastSeen(),
                request.departureAirport(),
                request.arrivalAirport());


        if((!nullRemover.CheckOneFlight(flight))) {

            flightRepo.save(flight);
            DataStorage.getInstance().addFlight(flight);
            DataStorage.getInstance().addEnrichedFlight(new EnrichedFlight(flight));

        }

        return flightToResponseMapper.apply(flight);
    }

    /**
     * Creates a list of flights and stores them in the database (used for POST request).
     * It enables client to upload all flights at once.
     * @return List of FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public List<FlightResponse> createBulk(List<FlightResponse> request) {
        List<Flight> flights = request.stream()
                .map(r -> new Flight(
                        r.icao24(),
                        r.firstSeen(),
                        r.lastSeen(),
                        r.departureAirport(),
                        r.arrivalAirport()))
                .collect(Collectors.toCollection(ArrayList::new));

        NullRemover nullRemover = new NullRemover();

        nullRemover.TransformFlights(flights);

        flightRepo.saveAll(flights);

        return flights.stream()
                .map(flightToResponseMapper)
                .toList();
    }


    /**
     * Replaces a flight stored in the database (used for PUT request)
     * @param id id of a flight - used to find a flight that should be replaced
     * @param request a flight - it needs all parameters specified
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public FlightResponse update(Long id, FlightResponse request) {
        Flight flight = flightRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "id", id));

        flight.setIcao24(request.icao24());
        flight.setFirstSeen(request.firstSeen());
        flight.setLastSeen(request.lastSeen());
        flight.setDepartureAirport(request.departureAirport());
        flight.setArrivalAirport(request.arrivalAirport());

        flightRepo.save(flight);

        return flightToResponseMapper.apply(flight);
    }


    /**
     * Updates a flight stored in the database (used for PATCH request)
     * @param id id of a flight - used to find a flight that should be updated
     * @param req a DTO - the request can contain any number of updated parameters with their values
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public FlightResponse patch(Long id, FlightUpdateRequest req) {
        Flight flight = flightRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "id", id));

        if (req.icao24() != null) flight.setIcao24(req.icao24());
        if (req.firstSeen() != null) flight.setFirstSeen(req.firstSeen());
        if (req.lastSeen() != null) flight.setLastSeen(req.lastSeen());
        if (req.departureAirport() != null) flight.setDepartureAirport(req.departureAirport());
        if (req.arrivalAirport() != null) flight.setArrivalAirport(req.arrivalAirport());

        flightRepo.save(flight);
        return flightToResponseMapper.apply(flight);
    }

    /**
     * Deletes a flight between two points in time
     * @param startDate date from which flights should be deleted
     * @param endDate date to which flights should be deleted
     */
    public void deleteByTimeRange(int startDate, int endDate) {
        flightRepo.deleteByFirstSeenGreaterThanEqualAndLastSeenLessThanEqual(startDate, endDate);
    }

    /**
     * Deletes a flight based on its id
     * @param id id of a flight
     */
    public void delete(Long id) {
        if (!flightRepo.existsById(id)) {
            throw new ResourceNotFoundException("Flight", "id", id);
        }
        flightRepo.deleteById(id);
    }
}
