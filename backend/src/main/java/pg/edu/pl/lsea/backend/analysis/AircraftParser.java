package pg.edu.pl.lsea.backend.analysis;

import pg.edu.pl.lsea.backend.entities.Aircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class creating a maping between icao24 and aircrafts.
 * Provides faster search.
 */
public class AircraftParser {
    private final Map<String, Aircraft> aircraftMap;

    /**
     * Constructor for AircraftParser, providing mapping.
     * @param aircraftList - list of aircrafts.
     */
    public AircraftParser(List<Aircraft> aircraftList) {
        this.aircraftMap = new HashMap<>();
        for (Aircraft a : aircraftList) {
            aircraftMap.put(a.getIcao24(), a);
        }
    }

    /**
     * Get back aircraft by providing Icao24
     * @param icao24 - icao24
     * @return Aircraft with specific Icao24
     */
    public Aircraft getAircraftByIcao(String icao24) {
        return aircraftMap.get(icao24);
    }
}
