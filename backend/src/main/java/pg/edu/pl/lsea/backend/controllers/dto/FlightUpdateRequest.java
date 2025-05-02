package pg.edu.pl.lsea.backend.controllers.dto;

public record FlightUpdateRequest(
        String icao24,
        Integer firstSeen,
        Integer lastSeen,
        String departureAirport,
        String arrivalAirport
) {}
