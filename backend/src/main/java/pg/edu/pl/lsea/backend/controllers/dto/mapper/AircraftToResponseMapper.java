package pg.edu.pl.lsea.backend.controllers.dto.mapper;

import org.springframework.stereotype.Component;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;

import java.util.function.Function;

/**
 * Maps Aircraft to AircraftResponse which is useful for converting data stored in database
 * to data that should be sent with API (done in AircraftService)
 */
@Component
public class AircraftToResponseMapper implements Function<Aircraft, AircraftResponse> {

    @Override
    public AircraftResponse apply(Aircraft aircraft) {
        return new AircraftResponse(
                aircraft.getId(),
                aircraft.getIcao24(),
                aircraft.getModel().getName(),
                aircraft.getOperator().getName(),
                aircraft.getOwner()
        );
    }
}
