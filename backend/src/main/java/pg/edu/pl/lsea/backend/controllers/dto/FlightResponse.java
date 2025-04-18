package pg.edu.pl.lsea.backend.controllers.dto;

public record FlightResponse (Long id, String icao24, int firstSeen, int lastSeen, String departureAirport, String arrivalAirport) {

}
