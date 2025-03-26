package pg.edu.pl.lsea.entities;

import java.util.Comparator;

/**
 * A class representing a tracked flight of an aircraft
 */
public class Flight extends Trackable {
    /**
     * Unix timestamp of the first record of the aircraft of the flight in seconds.
     */
    private int firstSeen;
    /**
     * Unix timestamp of the last record of the aircraft of the flight in seconds.
     */
    private int lastSeen;
    /**
     * IATA code of the airport from which the aircraft is taking off on this flight.
     */
    private String departureAirport;
    /**
     * IATA code of the airport where the aircraft lands during this flight.
     */
    private String arrivalAirport;

    /**
     * Creates a flight object.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     * @param firstSeen An integer representing the unix timestamp of the first record of the aircraft of the flight in seconds.
     * @param lastSeen An integer representing the unix timestamp of the last record of the aircraft of the flight in seconds.
     * @param departureAirport A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     * @param arrivalAirport A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public Flight(String icao24, int firstSeen, int lastSeen, String departureAirport, String arrivalAirport) {
        setIcao24(icao24);
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

   /**
     * Class for comparing flights based on firstSeen timestamp, lastSeen timestamp and departure airport.
     */
    public static class FlightComparator implements Comparator<Flight> {
       /**
        * Compares flights by firstSeen,
        * if firstSeen is the same compares by lastSeen,
        * if lastSeen is the same compares by departureAirport.
        * @param a The first flight to be compared.
        * @param b The second flight to be compared.
        * @return Negative integer if first object is smaller by comparison of firstSeen, lastSeen and departureAirport,
        * positive integer if first object is greater,
        * zero if both have the same firstSeen, lastSeen and departureAirport.
        */
        @Override
        public int compare(Flight a, Flight b) {
            int firstSeenCompare = a.firstSeen -b.firstSeen;
            int lastSeenCompare = a.lastSeen -b.lastSeen;
            int departureAirportCompare = a.departureAirport.compareTo(b.departureAirport);

            return (firstSeenCompare == 0)
                ? (lastSeenCompare == 0)
                    ? departureAirportCompare
                    : lastSeenCompare
                : firstSeenCompare;
        }
    }


    /**
     * Returns the unix timestamp of the first record of the aircraft during the tracked flight.
     * @return An integer representing the unix timestamp of the first record of the aircraft of the flight
     */
    public int getFirstSeen() {
        return firstSeen;
    }
    /**
     * Returns the unix timestamp of the last record of the aircraft during the tracked flight.
     * @return An integer representing the unix timestamp of the last record of the aircraft of the flight
     */
    public int getLastSeen() {
        return lastSeen;
    }

    /**
     * Returns the IATA code of the departure airport of the flight.
     * @return A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     */
    public String getDepartureAirport() {
        return departureAirport;
    }
    /**
     * Returns the IATA code of the arrival airport of the flight.
     * @return A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public String getArrivalAirport() {
        return arrivalAirport;
    }
    /**
     * Sets the unix timestamp of the first record of the aircraft during the tracked flight.
     * @param firstSeen An integer representing the unix timestamp of the first record of the aircraft of the flight
     */
    public void setFirstSeen(int firstSeen) {
        this.firstSeen = firstSeen;
    }

    /**
     * Sets the unix timestamp of the last record of the aircraft during the tracked flight.
     * @param lastSeen An integer representing the unix timestamp of the last record of the aircraft of the flight
     */
    public void setLastSeen(int lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     * Sets the departure airport of the flight.
     * @param departureAirport A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     */
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    /**
     * Sets the arrival airport of the flight.
     * @param arrivalAirport A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
    /**
     * Printing flights.
     * @return Flight with all fields in a string format.
     */
    @Override
    public String toString() {
        return "Flight{" +
               "icao24='" + getIcao24() + "'" +
               ", firstseen=" + firstSeen +
               ", lastseen=" + lastSeen +
               ", departureairport='" + departureAirport + "'" +
               ", arrivalairport='" + arrivalAirport + "'" +
               "}";
    }
}

