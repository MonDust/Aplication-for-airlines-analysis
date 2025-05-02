package pg.edu.pl.lsea.backend.controllers.dto;

/**
 * This record is responsible for handling what should be exposed via REST API endpoint.
 * Its fields (id, icao24, ...) are all that the client sees.
 * @param id Automatically generated ID by the database ORM
 * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
 * @param firstSeen Unix timestamp of the first record of the aircraft of the flight in seconds.
 * @param lastSeen Unix timestamp of the last record of the aircraft of the flight in seconds.
 * @param departureAirport IATA code of the airport from which the aircraft is taking off on this flight.
 * @param arrivalAirport IATA code of the airport where the aircraft lands during this flight.
 */
public record EnrichedFlightResponse (Long id, String icao24, int firstSeen, int lastSeen, String departureAirport, String arrivalAirport, int timeInAir) {

}
