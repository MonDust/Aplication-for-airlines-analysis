package pg.edu.pl.lsea.api.dto.mapper;

import pg.edu.pl.lsea.api.dto.FlightResponse;
import pg.edu.pl.lsea.entities.Flight;

/**
 * Mapper class responsible for mapping FlightResponse to Flight entity.
 */
public class ResponseToFlightMapper {
    /**
     * Maps a FlightResponse to a Flight entity.
     * @param response The FlightResponse object to map.
     * @return A Flight entity.
     */
    public static Flight mapToFlight(FlightResponse response) {
        if (response == null) {
            return new Flight();
        }
        return new Flight(
                response.icao24(),
                response.firstSeen(),
                response.lastSeen(),
                response.departureAirport(),
                response.arrivalAirport()
        );
    }
}
