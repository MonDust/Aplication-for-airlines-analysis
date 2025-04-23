package pg.edu.pl.lsea.backend.controllers.dto;

/**
 * This record is responsible for handling what should be exposed via REST API endpoint.
 * Its fields (id, icao24, ...) are all that the client sees.
 * @param id Automatically generated ID by the database ORM
 * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
 * @param model A string representing the model of the aircraft
 * @param operator A string representing the operator of the aircraft (for example an airline company)
 * @param owner A string representing the owner of the aircraft
 */
public record AircraftResponse (Long id, String icao24, String model, String operator, String owner) {
}
