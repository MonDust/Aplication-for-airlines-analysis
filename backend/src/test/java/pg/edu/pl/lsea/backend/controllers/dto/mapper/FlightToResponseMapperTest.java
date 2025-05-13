package pg.edu.pl.lsea.backend.controllers.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pg.edu.pl.lsea.backend.controllers.dto.FlightResponse;
import pg.edu.pl.lsea.backend.entities.Airport;
import pg.edu.pl.lsea.backend.entities.Route;
import pg.edu.pl.lsea.backend.entities.original.*;

import static org.junit.jupiter.api.Assertions.*;

class FlightToResponseMapperTest {

    private FlightToResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FlightToResponseMapper();
    }

    @Test
    void apply_mapsFlightToFlightResponseCorrectly() {
        Airport origin = new Airport();
        origin.setCode("AAA");

        Airport destination = new Airport();
        destination.setCode("BBB");

        Route route = new Route();
        route.setOrigin(origin);
        route.setDestination(destination);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setIcao24("ICAO123");
        flight.setFirstSeen(123456);
        flight.setLastSeen(123457);
        flight.setRoute(route);

        FlightResponse response = mapper.apply(flight);

        assertEquals(1L, response.id());
        assertEquals("ICAO123", response.icao24());
        assertEquals(123456, response.firstSeen());
        assertEquals(123457, response.lastSeen());
        assertEquals("AAA", response.departureAirport());
        assertEquals("BBB", response.arrivalAirport());
    }
}
