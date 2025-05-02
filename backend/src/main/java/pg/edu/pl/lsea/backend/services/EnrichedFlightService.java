package pg.edu.pl.lsea.backend.services;

import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.EnrichedFlightResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.List;

/**
 * Class responsible for flights REST API business logic (part of MVC architecture)
 * It gets data from EnrichedFlightRepo.
 */
@Service
public class EnrichedFlightService {
    private final EnrichedFlightRepo enrichedFlightRepo;
    private final EnrichedFlightToResponseMapper enrichedFlightToResponseMapper;

    public EnrichedFlightService(EnrichedFlightRepo enrichedFlightRepo,
                         EnrichedFlightToResponseMapper enrichedFlightToResponseMapper) {

        this.enrichedFlightRepo = enrichedFlightRepo;
        this.enrichedFlightToResponseMapper = enrichedFlightToResponseMapper;
    }

    /**
     * Returns the list of all flights stored in database (used for GET request)
     * @return List of FlightResponse (FlightResponse = DTO) is what should be exposed via REST API endpoint
     */
    public List<EnrichedFlightResponse> getAll() {
        return enrichedFlightRepo.findAll()
                .stream()
                .map(enrichedFlightToResponseMapper)
                .toList();
    }

    /**
     * Returns one particular flight stored in database (used for GET request)
     * @return FlightResponse (=DTO) is what should be exposed via REST API endpoint
     */
    public EnrichedFlightResponse getByIcao(String icao) {
        return enrichedFlightRepo.findByIcao24(icao)
                .map(enrichedFlightToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("EnrichedFlight", "icao24", icao));
    }
}
