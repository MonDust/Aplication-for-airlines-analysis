package pg.edu.pl.lsea.backend.data.engieniering;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pg.edu.pl.lsea.backend.entities.Airport;
import pg.edu.pl.lsea.backend.entities.Route;
import pg.edu.pl.lsea.backend.entities.original.Flight;

import static org.junit.jupiter.api.Assertions.*;

class NullRemoverTest {
    Flight flight;
    NullRemover remover;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        remover = new NullRemover();
    }

    @Test
    void checkOneFlight_withValidFlight_returnsTrue() {
        flight.setIcao24("ICAO123");
        flight.setRoute(new Route(new Airport("AAA"), new Airport("BBB")));
        flight.setFirstSeen(123456);
        flight.setLastSeen(123457);

        assertTrue(remover.CheckOneFlight(flight));
    }

    @Test
    void checkOneFlight_withNullIcao24_returnsFalse() {
        flight.setIcao24(null);
        flight.setRoute(new Route(new Airport("AAA"), new Airport("BBB")));
        flight.setFirstSeen(123456);
        flight.setLastSeen(123457);

        assertFalse(remover.CheckOneFlight(flight));
    }

    @Test
    void checkOneFlight_withEmptyRoute_returnsFalse() {
        flight.setIcao24("ICAO123");
        flight.setRoute(null);
        flight.setFirstSeen(123456);
        flight.setLastSeen(123457);

        assertFalse(remover.CheckOneFlight(flight));
    }

    @Test
    void checkOneFlight_withRouteLiteralNULL_returnsFalse() {
        flight.setIcao24("ICAO123");
        flight.setRoute(new Route(new Airport("NULL"), new Airport("BBB")));
        flight.setFirstSeen(123456);
        flight.setLastSeen(123457);

        assertFalse(remover.CheckOneFlight(flight));
    }

    @Test
    void checkOneFlight_withFirstSeenMinusOne_returnsFalse() {
        flight.setIcao24("ICAO123");
        flight.setRoute(new Route(new Airport("AAA"), new Airport("BBB")));
        flight.setFirstSeen(-1);
        flight.setLastSeen(123457);

        assertFalse(remover.CheckOneFlight(flight));
    }


}