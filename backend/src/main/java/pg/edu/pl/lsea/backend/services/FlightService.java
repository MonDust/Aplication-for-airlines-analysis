package pg.edu.pl.lsea.backend.services;

import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.backend.data.engieniering.NullRemover;
import pg.edu.pl.lsea.backend.data.storage.DataStorage;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Flight;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
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
public class FlightService {
    private final FlightRepo flightRepo;
    private final EnrichedFlightRepo enrichedFlightRepo;


    private final FlightToResponseMapper flightToResponseMapper;
    private final EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;


    private final DataEnrichment enrichmentTool = new DataEnrichment();
    private final NullRemover nullRemover = new NullRemover();



    public FlightService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper, EnrichedFlightRepo enrichedFlightRepo,
                         EnrichedFlightToResponseMapper enrichedFlightToResponseMapper) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

        this.enrichedFlightRepo = enrichedFlightRepo;
        this.enrichedFlightToResponseMapper = enrichedFlightToResponseMapper;
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
    public FlightResponse getByIcao(String icao) {
        return flightRepo.findByIcao24(icao)
                .map(flightToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "icao24", icao));
    }

    /**
     * Creates a particular flight and stores it in the database (used for POST request).
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint. It can be ignored.
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
            enrichedFlightRepo.save(new EnrichedFlight(flight));

            DataStorage.getInstance().addFlight(flight);
            DataStorage.getInstance().addEnrichedFlight(new EnrichedFlight(flight));

        }

        return flightToResponseMapper.apply(flight);
    }

    /**
     * Creates a list of flights and stores them in the database (used for POST request).
     * It enables client to upload all flights at once.
     * @return List of FlightResponse (=DTO) is what should be exposed via REST API endpoint. It can be ignored.
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

        enrichedFlightRepo.saveAll(enrichmentTool.CreateEnrichedListOfFlights(flights));

        DataStorage.getInstance().bulkAddFlights(flights);
        DataStorage.getInstance().bulkAddEnrichedFlights(enrichmentTool.CreateEnrichedListOfFlights(flights));

        return flights.stream()
                .map(flightToResponseMapper)
                .toList();
    }
}
