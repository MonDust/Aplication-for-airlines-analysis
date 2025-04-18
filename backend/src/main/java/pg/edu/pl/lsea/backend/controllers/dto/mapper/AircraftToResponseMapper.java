package pg.edu.pl.lsea.backend.controllers.dto.mapper;

import org.springframework.stereotype.Component;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.entities.Aircraft;

import java.util.function.Function;

@Component
public class AircraftToResponseMapper implements Function<Aircraft, AircraftResponse> {

    @Override
    public AircraftResponse apply(Aircraft aircraft) {
        return new AircraftResponse(
                aircraft.getId(),
                aircraft.getIcao24(),
                aircraft.getModel(),
                aircraft.getOperator(),
                aircraft.getOwner()
        );
    }
}
