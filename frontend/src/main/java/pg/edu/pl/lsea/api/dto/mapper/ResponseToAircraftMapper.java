package pg.edu.pl.lsea.api.dto.mapper;

import pg.edu.pl.lsea.api.dto.AircraftResponse;
import pg.edu.pl.lsea.entities.Aircraft;

/**
 * Mapper class responsible for mapping AircraftResponse to Aircraft entity.
 */
public class ResponseToAircraftMapper {
    /**
     * Maps an AircraftResponse to an Aircraft entity.
     * @param response The AircraftResponse object to map.
     * @return An Aircraft entity.
     */
    public static Aircraft mapToAircraft(AircraftResponse response) {
        if (response == null) {
            return new Aircraft();
        }

        return new Aircraft(
                response.icao24(),
                response.model(),
                response.operator(),
                response.owner()
        );
    }
}
