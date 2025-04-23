package pg.edu.pl.lsea.backend.controllers.dto.mapper;

import org.springframework.stereotype.Component;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.entities.Flight;

import java.util.function.Function;

/**
 * Maps Flight to FlightResponse which is useful for converting data stored in database
 * to data that should be sent with API (done in FlightService)
 */
@Component
public class FlightToResponseMapper implements Function<Flight, FlightResponse> {

    @Override
    public FlightResponse apply(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getIcao24(),
                flight.getFirstSeen(),
                flight.getLastSeen(),
                flight.getDepartureAirport(),
                flight.getArrivalAirport()
        );
    }
}
