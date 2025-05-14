package pg.edu.pl.lsea.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.FlightUpdateRequest;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.backend.data.engieniering.NullRemover;
import pg.edu.pl.lsea.backend.entities.*;
import pg.edu.pl.lsea.backend.entities.Airport;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.original.Flight;
import pg.edu.pl.lsea.backend.repositories.*;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.*;


import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final AircraftRepo aircraftRepo;

    private final RouteRepo routeRepo;

    private final FlightToResponseMapper flightToResponseMapper;

    private final DataEnrichment enrichmentTool = new DataEnrichment();
    private final NullRemover nullRemover = new NullRemover();


    public FlightService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper, EnrichedFlightRepo enrichedFlightRepo,
                         AirportRepo airportRepo,
                         AircraftRepo aircraftRepo, RouteRepo routeRepo) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        this.enrichedFlightRepo = enrichedFlightRepo;

        this.airportRepo = airportRepo;
        this.aircraftRepo = aircraftRepo;
        this.routeRepo = routeRepo;
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

        return !Objects.equals(req.departureAirport(), null) && !Objects.equals(req.arrivalAirport(), null);
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

        Optional<Route> existingRoute = routeRepo.findByOriginAndDestination(arrivalAirport, departureAirport);
        Route route;
        if (existingRoute.isEmpty()) {
            route = new Route(departureAirport, arrivalAirport);
            routeRepo.save(route);
        } else {
            route = existingRoute.get();
        }

        // Handle an aircraft for the flight
        Optional<Aircraft> existingAircraft = aircraftRepo.findByIcao24(request.icao24());

        Flight flight = new Flight(
                request.icao24(),
                request.firstSeen(),
                request.lastSeen()
        );
        if (existingAircraft.isPresent()) {
            flight.setAircraft(existingAircraft.get());
            route.addOperator(existingAircraft.get().getOperator());
        }

        route.addFlight(flight);
        flight.setRoute(route);

        if(nullRemover.CheckOneFlight(flight)) {
            try {
                flightRepo.save(flight);
                enrichedFlightRepo.save(new EnrichedFlight(flight));
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error saving flight: " + ex.getMessage());
            }
        }

        System.out.println(flight);
        System.out.println(flightToResponseMapper.apply(flight));

        return flightToResponseMapper.apply(flight);
    }

    /**
     * Creates a list of flights and stores them in the database (used for POST request).
     * Also, adds all the airport objects if necessary
     * It enables client to upload all flights at once.
     * @return List of FlightResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public List<FlightResponse> createBulk(List<FlightResponse> request) {
        List<FlightResponse> validRequests = request.stream()
                .filter(this::checkRequestValidity)
                .toList();

        Set<String> airportCodes = validRequests.stream()
                .flatMap(r -> Stream.of(r.departureAirport(), r.arrivalAirport()))
                .collect(Collectors.toSet());

        // Lookup
        Map<String, Airport> airportMap = airportRepo.findAll().stream()
                .collect(Collectors.toMap(Airport::getCode, a -> a));

        List<Airport> newAirports = new ArrayList<>();
        for (String code : airportCodes) {
            if (!airportMap.containsKey(code)) {
                Airport airport = new Airport(code);
                airportMap.put(code, airport);
                newAirports.add(airport);
            }
        }

        // Save new airports before using them in flights
        airportRepo.saveAll(newAirports);

        Set<String> icao24Set = validRequests.stream()
                .map(FlightResponse::icao24)
                .collect(Collectors.toSet());

        Map<String, Aircraft> aircraftMap = aircraftRepo.findByIcao24In(icao24Set).stream()
                .collect(Collectors.toMap(Aircraft::getIcao24, a -> a));

        // Route lookup
        List<Route> existingRoutes = routeRepo.findAll();
        Map<String, Route> routeMap = new HashMap<>();
        for (Route route : existingRoutes) {
            String key = route.getOrigin().getCode() + "->" + route.getDestination().getCode();
            routeMap.put(key, route);
        }

        //List<Flight> flights = new ArrayList<>();

        for (FlightResponse r : validRequests) {
            String routeKey = r.departureAirport() + "->" + r.arrivalAirport();
            if (!routeMap.containsKey(routeKey)) {
                Airport dep = airportMap.get(r.departureAirport());
                Airport arr = airportMap.get(r.arrivalAirport());
                Route route = new Route(dep, arr);
                routeMap.put(routeKey, route);
            }
        }
        List<Route> newRoutes = routeMap.values().stream()
                .filter(r -> r.getId() == null)
                .toList();
        routeRepo.saveAll(newRoutes);

        List<Flight> flights = new ArrayList<>();
        for (FlightResponse r : validRequests) {
            Airport dep = airportMap.get(r.departureAirport());
            Airport arr = airportMap.get(r.arrivalAirport());
            String routeKey = dep.getCode() + "->" + arr.getCode();
            Route route = routeMap.get(routeKey);

            Flight flight = new Flight(
                    r.icao24(),
                    r.firstSeen(),
                    r.lastSeen()
            );

            Aircraft aircraft = aircraftMap.get(r.icao24());
            if (aircraft != null) {
                flight.setAircraft(aircraft);
                aircraft.getFlights().add(flight);
                route.addOperator(aircraft.getOperator());
            }

            route.addFlight(flight);
            flight.setRoute(route);

            flights.add(flight);
        }



        List<Flight> newFlights = flights.stream()
                .filter(f -> !checkIfFlightExists(f.getIcao24(), f.getFirstSeen()))
                .toList();

        try {
            flightRepo.saveAll(newFlights);
            enrichedFlightRepo.saveAll(enrichmentTool.CreateEnrichedListOfFlights(newFlights));
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving: " + ex.getMessage());
        }

        return newFlights.stream()
                .map(flightToResponseMapper)
                .toList();
    }

    private void updateAirport(String airportDepartureCode, String airportArrivalCode) {
        Airport departureAirport;
        Optional<Airport> existingDepartureAirport = airportRepo.findByCode(airportDepartureCode);
        if (existingDepartureAirport.isPresent()) {
            departureAirport = existingDepartureAirport.get();
        }
        else {
            departureAirport = new Airport(airportDepartureCode);
            airportRepo.save(departureAirport);
        }

        Airport arrivalAirport;
        Optional<Airport> existingArrivalAirport = airportRepo.findByCode(airportArrivalCode);
        if (existingArrivalAirport.isPresent()) {
            arrivalAirport = existingArrivalAirport.get();
        }
        else {
            arrivalAirport = new Airport(airportDepartureCode);
            airportRepo.save(arrivalAirport);
        }
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
        updateAirport(request.departureAirport(), request.arrivalAirport());

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
        updateAirport(req.departureAirport(), req.arrivalAirport());

        try {
            flightRepo.save(flight);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving flight: " + ex.getMessage());
        }
        return flightToResponseMapper.apply(flight);
    }

    /**
     * Updates a flight stored in the database using ICAO24 and firstSeen as identifiers (used for PATCH request).
     * @param icao24 the ICAO24 of the aircraft – used to identify the flight
     * @param firstSeen the timestamp of the first record – used to identify the flight
     * @param req DTO – can contain any number of updated parameters with their values
     * @return FlightResponse DTO to be exposed via REST API
     */
    @Transactional
    public FlightResponse patchByIcao24AndFirstSeen(String icao24, int firstSeen, FlightUpdateRequest req) {
        Flight flight = flightRepo.findByIcao24AndFirstSeen(icao24,firstSeen)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "icao24", icao24));

        if (req.icao24() != null) flight.setIcao24(req.icao24());
        if (req.firstSeen() != null) flight.setFirstSeen(req.firstSeen());
        if (req.lastSeen() != null) flight.setLastSeen(req.lastSeen());
        updateAirport(req.departureAirport(), req.arrivalAirport());

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
