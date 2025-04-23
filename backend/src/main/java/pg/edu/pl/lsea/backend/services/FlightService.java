package pg.edu.pl.lsea.backend.services;

import org.springframework.stereotype.Service;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
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

@Service
public class FlightService {
    private final FlightRepo flightRepo;
    private final FlightToResponseMapper flightToResponseMapper;
    private final DataEnrichment enrichmentTool = new DataEnrichment();
    private final NullRemover nullRemover = new NullRemover();



    public FlightService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper) {
        this.flightRepo = flightRepo;
        this.flightToResponseMapper = flightToResponseMapper;

    }

    public List<FlightResponse> getAll() {
        return flightRepo.findAll()
                .stream()
                .map(flightToResponseMapper)
                .toList();
    }

    public FlightResponse getByIcao(String icao) {
        return flightRepo.findById(icao)
                .map(flightToResponseMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Flight", "icao24", icao));
    }

    public FlightResponse create(FlightResponse request) {
        Flight flight = new Flight(
                request.icao24(),
                request.firstSeen(),
                request.lastSeen(),
                request.departureAirport(),
                request.arrivalAirport());


        if(       (!nullRemover.CheckOneFlight(flight))) {

            flightRepo.save(flight);
            DataStorage.getInstance().addFlight(flight);
            DataStorage.getInstance().addEnrichedFlight(new EnrichedFlight(flight));

        }

        return flightToResponseMapper.apply(flight);
    }

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
        DataStorage.getInstance().bulkAddFlights(flights);
        DataStorage.getInstance().bulkAddEnrichedFlights(enrichmentTool.CreateEnrichedListOfFlights(flights));



        return flights.stream()
                .map(flightToResponseMapper)
                .toList();
    }
}
