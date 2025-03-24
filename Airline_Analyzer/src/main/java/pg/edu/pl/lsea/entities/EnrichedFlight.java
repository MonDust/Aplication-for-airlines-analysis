package pg.edu.pl.lsea.entities;

public class EnrichedFlight extends Trackable{
    private int timeinair;
    /**
     * Unix timestamp of the first record of the aircraft of the flight in seconds.
     */
    private int firstseen;
    /**
     * Unix timestamp of the last record of the aircraft of the flight in seconds.
     */
    private int lastseen;
    /**
     * IATA code of the airport from which the aircraft is taking off on this flight.
     */
    private String departureairport;
    /**
     * IATA code of the airport where the aircraft lands during this flight.
     */
    private String arrivalairport;

    /**
     * Creates a flight object.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     * @param firstseen An integer representing the unix timestamp of the first record of the aircraft of the flight in seconds.
     * @param lastseen An integer representing the unix timestamp of the last record of the aircraft of the flight in seconds.
     * @param timeinair An integer representing time in the air in seconds.
     * @param departureairport A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     * @param arrivalairport A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public EnrichedFlight(String icao24, int firstseen, int lastseen, String departureairport, String arrivalairport) {
        setIcao24(icao24);
        this.firstseen = firstseen;
        this.lastseen = lastseen;
        this.departureairport = departureairport;
        this.arrivalairport = arrivalairport;
        updateTimeintheair(lastseen, firstseen);
    }

    /**
     * Returns the unix timestamp of the first record of the aircraft during the tracked flight.
     * @return An integer representing the unix timestamp of the first record of the aircraft of the flight
     */
    public int getFirstseen() {
        return firstseen;
    }
    /**
     * Returns the unix timestamp of the last record of the aircraft during the tracked flight.
     * @return An integer representing the unix timestamp of the last record of the aircraft of the flight
     */
    public int getLastseen() {
        return lastseen;
    }

    /**
     * Returns the integer representing time in the air in seconds.
     * @return An integer representing time in the air in seconds
     */
    public int getTimeinair() {
        return timeinair;
    }

    /**
     * Returns the IATA code of the departure airport of the flight.
     * @return A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     */
    public String getDepartureairport() {
        return departureairport;
    }
    /**
     * Returns the IATA code of the arrival airport of the flight.
     * @return A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public String getArrivalairport() {
        return arrivalairport;
    }
    /**
     * Sets the unix timestamp of the first record of the aircraft during the tracked flight.
     * @param firstseen An integer representing the unix timestamp of the first record of the aircraft of the flight
     */
    public void setFirstseen(int firstseen) {
        this.firstseen = firstseen;
    }

    /**
     * Sets the unix timestamp of the last record of the aircraft during the tracked flight.
     * @param lastseen An integer representing the unix timestamp of the last record of the aircraft of the flight
     */
    public void setLastseen(int lastseen) {
        this.lastseen = lastseen;
    }


    /**
     * Sets the integer representing time in the air in seconds.       .
     */
    private void updateTimeintheair(int firstseen, int lastseen) {
        this.timeinair = firstseen - lastseen;
    }
    /**
     * Sets the departure airport of the flight.
     * @param departureairport A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     */
    public void setDepartureairport(String departureairport) {
        this.departureairport = departureairport;
    }

    /**
     * Sets the arrival airport of the flight.
     * @param arrivalairport A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public void setArrivalairport(String arrivalairport) {
        this.arrivalairport = arrivalairport;
    }

}
