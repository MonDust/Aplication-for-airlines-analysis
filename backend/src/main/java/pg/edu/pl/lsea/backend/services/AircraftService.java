package pg.edu.pl.lsea.backend.services;

import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.utils.ResourceNotFoundException;

import java.util.List;

@Service
public class AircraftService {
    private final AircraftRepo aircraftRepo;
    private final AircraftToResponseMapper aircraftToResponseMapper;

    public AircraftService(AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper) {
        this.aircraftRepo = aircraftRepo;
        this.aircraftToResponseMapper = aircraftToResponseMapper;
    }


    public List<AircraftResponse> getAll() {
        return aircraftRepo.findAll()
                .stream()
                .map(aircraftToResponseMapper)
                .toList();
    }

    public AircraftResponse getByIcao(String icao) {
        return aircraftRepo.findById(icao)
                .map(aircraftToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "icao24", icao));
    }

    public AircraftResponse create(AircraftResponse request) {
        Aircraft aircraft = new Aircraft(
                request.icao24(),
                request.model(),
                request.operator(),
                request.owner()
        );

        aircraftRepo.save(aircraft);
        return aircraftToResponseMapper.apply(aircraft);
    }

    public List<AircraftResponse> createBulk(List<AircraftResponse> request) {
        List<Aircraft> aircrafts = request.stream()
                .map(a -> new Aircraft(
                        a.icao24(),
                        a.model(),
                        a.operator(),
                        a.owner()
                ))
                .toList();

        aircraftRepo.saveAll(aircrafts); // More efficient than saving one-by-one

        return aircrafts.stream()
                .map(aircraftToResponseMapper)
                .toList();
    }

}
