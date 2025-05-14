package pg.edu.pl.lsea.backend.controllers.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pg.edu.pl.lsea.backend.controllers.dto.AircraftResponse;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AircraftToResponseMapperTest {

    private AircraftToResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AircraftToResponseMapper();
    }

    @Test
    void apply_mapsAircraftToFlightResponseCorrectly() {
        Model model = new Model("Boeing 737");
        Operator operator = new Operator("Delta Airlines");

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1L);
        aircraft.setIcao24("ICAO123");
        aircraft.setModel(model);
        aircraft.setOperator(operator);
        aircraft.setOwner("USA");

        AircraftResponse response = mapper.apply(aircraft);

        assertEquals(1L, response.id());
        assertEquals("ICAO123", response.icao24());
        assertEquals("Boeing 737", response.model());
        assertEquals("Delta Airlines", response.operator());
        assertEquals("USA", response.owner());
    }
}
