package pg.edu.pl.lsea.backend.controllers.dto;

public record AircraftResponse (Long id, String icao24, String model, String operator, String owner) {
}
