package pg.edu.pl.lsea.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightUpdateRequest;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.backend.data.engieniering.NullRemover;
import pg.edu.pl.lsea.backend.entities.*;
import pg.edu.pl.lsea.backend.repositories.AirportRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;
import java.util.ArrayList;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class responsible for flights REST API business logic (part of MVC architecture)
 * It gets and saves data to/from FlightRepo.
 */
@Service
@Transactional
public class FlightService {
    private final FlightRepo flightRepo;
    private final EnrichedFlightRepo enrichedFlightRepo;

    private final AirportRepo airportRepo;

    private final FlightToResponseMapper flightToResponseMapper;
    private final EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;

    private final DataEnrichment enrichmentTool = new DataEnrichment();
    private final NullRemover nullRemover = new NullRemover();



    public FlightService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper, EnrichedFlightRepo enrichedFlightRepo,
                         AirportRepo airportRepo,
                         EnrichedFlightToResponseMapper enrichedFlightToResponseMapper) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        this.enrichedFlightRepo = enrichedFlightRepo;
        this.enrichedFlightToResponseMapper = enrichedFlightToResponseMapper;

        this.airportRepo = airportRepo;
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
     * Check if flight exists.
     * @param icao24 - ICAO
     * @param firstSeen - FIRST SEEN
     * @return - boolean true if exists, false if not.
     */
    private boolean checkIfFlightExists(String icao24, int firstSeen) {
        Optional<Flight> existingFlight = flightRepo.findByIcao24AndFirstSeen(icao24, firstSeen);
        return existingFlight.isPresent();
    }

    /**
     * Checks if the flight request is valid
     */
    private boolean checkRequestValidity(FlightResponse req) {
        if (Objects.equals(req.departureAirport(), "") || Objects.equals(req.arrivalAirport(), "")) {
            return false;
        }

        if (Objects.equals(req.departureAirport(), "NULL") || Objects.equals(req.arrivalAirport(), "NULL")) {
            return false;
        }

        if (Objects.equals(req.departureAirport(), null) || Objects.equals(req.arrivalAirport(), null)) {
            return false;
        }

        return true;
    }

    /**
     * Creates a particular flight and stores it in the database (used for POST request).
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public FlightResponse create(FlightResponse request) {
        if (!checkRequestValidity(request)) {
            return null;
        }

        // Check if the flight already exists
        if (checkIfFlightExists(request.icao24(), request.firstSeen())) {
            // System.out.println("Flight with ICAO " + flight.getIcao24() + " and first seen " + flight.getFirstSeen() + " already exists.");
            return null;
        }

        // Find or create departure Airport
        Optional<Airport> existingDepartureAirport = airportRepo.findByCode(request.departureAirport());
        Airport departureAirport;
        if (existingDepartureAirport.isEmpty()) {
            departureAirport = new Airport(request.departureAirport());
            airportRepo.save(departureAirport);
        }
        else {
            departureAirport = existingDepartureAirport.get();
        }

        // Find or create arrival Airport
        Optional<Airport> existingArrivalAirport = airportRepo.findByCode(request.arrivalAirport());
        Airport arrivalAirport;
        if (existingArrivalAirport.isEmpty()) {
            arrivalAirport = new Airport(request.arrivalAirport());
            airportRepo.save(arrivalAirport);
        }
        else {
            arrivalAirport = existingArrivalAirport.get();
        }

        Flight flight = new Flight(
                request.icao24(),
                request.firstSeen(),
                request.lastSeen(),
                departureAirport,
                arrivalAirport
        );

        if((!nullRemover.CheckOneFlight(flight))) {
            try {
                flightRepo.save(flight);
                enrichedFlightRepo.save(new EnrichedFlight(flight));
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error saving flight: " + ex.getMessage());
            }
        }

        return flightToResponseMapper.apply(flight);
    }

    /**
     * Creates a list of flights and stores them in the database (used for POST request).
     * Also, adds all the airport objects if necessary
     * It enables client to upload all flights at once.
     * @return List of FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public List<FlightResponse> createBulk(List<FlightResponse> request) {
        List<FlightResponse> validRequests = request.stream().
                filter(this::checkRequestValidity).
                toList();

        List<Airport> existingAirports = airportRepo.findAll();
        List<Airport> newAirports = new ArrayList<>();

        List<Flight> flights = validRequests.stream()
                .map(r -> {

                    // Find or create departure airport
                    Optional<Airport> existingDepartureAirport = existingAirports.stream()
                            .filter(o -> o.getCode().equals(r.departureAirport()))
                            .findFirst();

                    Airport departureAirport;
                    if (existingDepartureAirport.isEmpty()) {
                        departureAirport = new Airport(r.departureAirport());
                        existingAirports.add(departureAirport);
                        newAirports.add(departureAirport);
                    }
                    else {
                        departureAirport = existingDepartureAirport.get();
                    }

                    // Find or create arrival airport
                    Optional<Airport> existingArrivalAirport = existingAirports.stream()
                            .filter(o -> o.getCode().equals(r.arrivalAirport()))
                            .findFirst();

                    Airport arrivalAirport;
                    if (existingArrivalAirport.isEmpty()) {
                        arrivalAirport = new Airport(r.arrivalAirport());
                        existingAirports.add(arrivalAirport);
                        newAirports.add(arrivalAirport);
                    }
                    else {
                        arrivalAirport = existingArrivalAirport.get();
                    }


                    Flight newFlight = new Flight(
                            r.icao24(),
                            r.firstSeen(),
                            r.lastSeen(),
                            departureAirport,
                            arrivalAirport
                    );

                    departureAirport.getDepartureFlights().add(newFlight);
                    arrivalAirport.getArrivalFlights().add(newFlight);

                    return newFlight;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        NullRemover nullRemover = new NullRemover();
        nullRemover.TransformFlights(flights);

        // Filter out flights that already exist
        List<Flight> newFlights = flights.stream()
                .filter(f -> !checkIfFlightExists(f.getIcao24(), f.getFirstSeen()))
                .toList();

        try {
            flightRepo.saveAll(newFlights);
            enrichedFlightRepo.saveAll(enrichmentTool.CreateEnrichedListOfFlights(newFlights));
            airportRepo.saveAll(newAirports);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving : " + ex.getMessage());
        }

        List<Airport> test_airports = airportRepo.findAll();
        System.out.println("TEST - airports: " + test_airports.size());

        return newFlights.stream()
                .map(flightToResponseMapper)
                .toList();
    }


    private void updateAirport(Flight flight, String airportDepartureCode, String airportArrivalCode) {
        Airport departureAirport;
        Optional<Airport> existingDepartureAirport = airportRepo.findByCode(airportDepartureCode);
        if (existingDepartureAirport.isPresent()) {
            departureAirport = existingDepartureAirport.get();
            flight.setDepartureAirport(departureAirport);
        }
        else {
            departureAirport = new Airport(airportDepartureCode);
            airportRepo.save(departureAirport);
            flight.setDepartureAirport(departureAirport);
        }

        Airport arrivalAirport;
        Optional<Airport> existingArrivalAirport = airportRepo.findByCode(airportArrivalCode);
        if (existingArrivalAirport.isPresent()) {
            arrivalAirport = existingArrivalAirport.get();
            flight.setArrivalAirport(arrivalAirport);
        }
        else {
            arrivalAirport = new Airport(airportDepartureCode);
            airportRepo.save(arrivalAirport);
            flight.setArrivalAirport(arrivalAirport);
        }

        departureAirport.getDepartureFlights().add(flight);
        arrivalAirport.getArrivalFlights().add(flight);
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

        //Optional<Airport> existingDepartureAirport = airportRepo.findByCode(request.departureAirport());
        //Optional<Airport> existingArrivalAirport = airportRepo.findByCode(request.arrivalAirport());

        flight.setIcao24(request.icao24());
        flight.setFirstSeen(request.firstSeen());
        flight.setLastSeen(request.lastSeen());
        updateAirport(flight, request.departureAirport(), request.arrivalAirport());

        try {
            flightRepo.save(flight);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving flight: " + ex.getMessage());
        }

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
        updateAirport(flight, req.departureAirport(), req.arrivalAirport());

        try {
            flightRepo.save(flight);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving flight: " + ex.getMessage());
        }
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
