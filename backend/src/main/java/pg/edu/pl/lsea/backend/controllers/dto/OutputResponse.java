package pg.edu.pl.lsea.backend.controllers.dto;

/**
 * Entity that handles output from analysed functions
 * @param id ID of this entity
 * @param icao24 ID of plane
 * @param Value value that is transferred about this plane
 */
public record  OutputResponse (Long id, String icao24, int Value) {

}
