package pg.edu.pl.lsea.backend.controllers.dto.mapper;

import org.springframework.stereotype.Component;
import pg.edu.pl.lsea.backend.controllers.dto.EnrichedFlightResponse;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.function.Function;

/**
 * Maps Flight to FlightResponse which is useful for converting data stored in database
 * to data that should be sent with API (done in FlightService)
 */
@Component
public class EnrichedFlightToResponseMapper implements Function<EnrichedFlight, EnrichedFlightResponse> {

    @Override
    public EnrichedFlightResponse apply(EnrichedFlight flight) {
        return new EnrichedFlightResponse(
                flight.getId(),
                flight.getIcao24(),
                flight.getFirstSeen(),
                flight.getLastSeen(),
                flight.getDepartureAirport(),
                flight.getArrivalAirport(),
                flight.getTimeInAir()
        );
    }
}
