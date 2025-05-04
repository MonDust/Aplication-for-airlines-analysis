package pg.edu.pl.lsea.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.data.engieniering.NullRemover;

import pg.edu.pl.lsea.backend.entities.Aircraft;

import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Class responsible for aircraft REST API business logic (part of MVC architecture)
 * It gets and saves data to/from AircraftRepo.
 */
@Service
@Transactional
public class AircraftService {
    private final AircraftRepo aircraftRepo;
    private final AircraftToResponseMapper aircraftToResponseMapper;

    /**
     * Constructor of the class.
     * @param aircraftRepo - aircraft repository
     * @param aircraftToResponseMapper - mapper
     */
    public AircraftService(AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper) {
        this.aircraftRepo = aircraftRepo;
        this.aircraftToResponseMapper = aircraftToResponseMapper;
    }

    /**
     * Returns the list of all aircrafts stored in database (used for GET request)
     * @return List of AircraftResponse (AircraftResponse = DTO) is what should be exposed via REST API endpoint
     */
    public List<AircraftResponse> getAll() {
        return aircraftRepo.findAll()
                .stream()
                .map(aircraftToResponseMapper)
                .toList();
    }

    /**
     * Returns one particular aircraft stored in database (used for GET request)
     * @return AircraftResponse (=DTO) is what should be exposed via REST API endpoint
     */
    public AircraftResponse getByIcao(String icao) {
        return aircraftRepo.findByIcao24(icao)
                .map(aircraftToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "icao24", icao));
    }

    /**
     * Check if aircraft exists.
     * @param icao24 - ICAO
     * @return - boolean true if exists, false if not.
     */
    private boolean checkIfAircraftExists(String icao24) {
        Optional<Aircraft> existingAircraft = aircraftRepo.findByIcao24(icao24);
        return existingAircraft.isPresent();
    }

    /**
     * Creates a particular aircraft and stores it in the database (used for POST request).
     * @return AircraftResponse (=DTO) is what should be exposed via REST API endpoint. It can be ignored.
     */
    public AircraftResponse create(AircraftResponse request) {
        Aircraft aircraft = new Aircraft(
                request.icao24(),
                request.model(),
                request.operator(),
                request.owner()
        );

        // Check if the aircraft already exists
        if (checkIfAircraftExists(aircraft.getIcao24())) {
            // System.out.println("Flight with ICAO " + aircraft.getIcao24() + " already exists.");
            return aircraftToResponseMapper.apply(aircraft);
        }

        try {
            aircraftRepo.save(aircraft);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving aircraft: " + ex.getMessage());
        }

        return aircraftToResponseMapper.apply(aircraft);
    }

    /**
     * Creates a list of aircrafts and stores them in the database (used for POST request).
     * It enables client to upload all aircrafts at once.
     * @return List of AircraftResponse (=DTO) is what should be exposed via REST API endpoint. It can be ignored.
     */
    public List<AircraftResponse> createBulk(List<AircraftResponse> request) {
        List<Aircraft> aircrafts = request.stream()
                .map(a -> new Aircraft(
                        a.icao24(),
                        a.model(),
                        a.operator(),
                        a.owner()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        NullRemover nullRemover = new NullRemover();
        nullRemover.TransformAircrafts(aircrafts);

        List<Aircraft> newAircraft = new ArrayList<>();
        for (Aircraft aircraft : aircrafts) {
            // Check if the flight already exists
            if (!checkIfAircraftExists(aircraft.getIcao24())) {
                newAircraft.add(aircraft);
            } else {
                // System.out.println("Flight with ICAO " + aircraft.getIcao24() + " already exists.");
            }
        }

        if (!newAircraft.isEmpty()) {
            try {
                aircraftRepo.saveAll(aircrafts);
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error saving to the Aircraft Repo: " + ex.getMessage());
            }
        }

        return aircrafts.stream()
                .map(aircraftToResponseMapper)
                .toList();
    }


    /**
     * Replaces an aircraft stored in the database (used for PUT request)
     * @param icao24 a unique id of an aircraft - used to find an aircraft that should be replaced
     * @param request an aircraft - it needs all parameters specified
     * @return AircraftResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public AircraftResponse updateAircraft(String icao24, AircraftResponse request) {
        Aircraft aircraft = aircraftRepo.findByIcao24(icao24)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "icao24", icao24));

        aircraft.setModel(request.model());
        aircraft.setOperator(request.operator());
        aircraft.setOwner(request.owner());

        try {
            aircraftRepo.save(aircraft);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving aircraft: " + ex.getMessage());
        }
        return aircraftToResponseMapper.apply(aircraft);
    }

    /**
     * Updates an aircraft stored in the database (used for PATCH request)
     * @param icao24 a unique id of an aircraft - used to find an aircraft that should be updated
     * @param request an aircraft - you can specify any number of parameters and their values
     * @return AircraftResponse (=DTO) is what should be exposed via REST API endpoint.
     */
    public AircraftResponse patchAircraft(String icao24, AircraftResponse request) {
        Aircraft aircraft = aircraftRepo.findByIcao24(icao24)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "icao24", icao24));

        if (request.model() != null) aircraft.setModel(request.model());
        if (request.operator() != null) aircraft.setOperator(request.operator());
        if (request.owner() != null) aircraft.setOwner(request.owner());

        try {
            aircraftRepo.save(aircraft);
        } catch (DataIntegrityViolationException ex) {
            System.err.println("Error saving aircraft: " + ex.getMessage());
        }
        return aircraftToResponseMapper.apply(aircraft);
    }

    /**
     * Deletes an aircraft based on icao24
     * @param icao24 a unique id of an aircraft
     */
    public void deleteAircraft(String icao24) {
        Aircraft aircraft = aircraftRepo.findByIcao24(icao24)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "icao24", icao24));

        aircraftRepo.delete(aircraft);
    }

}
