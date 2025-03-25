package pg.edu.pl.lsea.entities;

import java.util.Comparator;

public class EnrichedFlight extends Flight implements Comparable<EnrichedFlight>, Comparator<EnrichedFlight> {

    int timeInAir;
    /**
     * Creates a flight object.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     * @param firstSeen An integer representing the unix timestamp of the first record of the aircraft of the flight in seconds.
     * @param lastSeen An integer representing the unix timestamp of the last record of the aircraft of the flight in seconds.
     * @param departureAirport A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     * @param arrivalAirport A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public EnrichedFlight(String icao24, int firstSeen, int lastSeen, String departureAirport, String arrivalAirport) {
        super(icao24, firstSeen, lastSeen, departureAirport, arrivalAirport);
        updateTimeintheair(lastSeen, firstSeen);
    }

    /**
     * Returns the integer representing time in the air in seconds.
     * @return An integer representing time in the air in seconds
     */
    public int getTimeInAir() {
        return timeInAir;
    }


    /**
     * Sets the integer representing time in the air in seconds.       .
     */
    private void updateTimeintheair(int firstSeen, int lastSeen) {
        this.timeInAir = firstSeen - lastSeen;
    }


    @Override
    public int compareTo(EnrichedFlight o) {
        return Integer.compare(this.timeInAir, o.timeInAir);
    }

    @Override
    public int compare(EnrichedFlight o1, EnrichedFlight o2) {
        return Integer.compare(o1.getTimeInAir(), o2.getTimeInAir());
    }
}