package pg.edu.pl.lsea.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Objects;

/**
 * A class representing a tracked and enriched flight of an aircraft
 */
@Setter
@Getter
@Entity
@Table(name = "enrichedFlights")
public class EnrichedFlight extends Trackable implements Cloneable {
    /**
     * This value is generated automatically during object creation
     * and is added by the JPA (Java Persistence API).
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Unix timestamp of the first record of the aircraft of the flight in seconds.
     */
    @Column(name = "first_seen")
    private int firstSeen;

    /**
     * Unix timestamp of the last record of the aircraft of the flight in seconds.
     */
    @Column(name = "last_seen")
    private int lastSeen;

    /**
     * IATA code of the airport from which the aircraft is taking off on this flight.
     */
    @Column(name = "departure_airport")
    private String departureAirport;

    /**
     * IATA code of the airport where the aircraft lands during this flight.
     */
    @Column(name = "arrival_airport")
    private String arrivalAirport;

    /**
     * Duration of the flight in seconds.
     * Time in air in seconds for the aircraft during the flight.
     */
    @Column(name = "time_in_air", nullable = false)
    private int timeInAir;


    /**
     * Creates a flight object.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     * @param firstSeen An integer representing the unix timestamp of the first record of the aircraft of the flight in seconds.
     * @param lastSeen An integer representing the unix timestamp of the last record of the aircraft of the flight in seconds.
     * @param departureAirport A string representing the IATA code of the airport from which the aircraft is taking off on this flight
     * @param arrivalAirport A string representing the IATA code of the airport where the aircraft lands during this flight
     */
    public EnrichedFlight(String icao24, int firstSeen, int lastSeen, String departureAirport, String arrivalAirport) {
        setIcao24(icao24);
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.timeInAir =0;
        updateTimeInAir(lastSeen, firstSeen);

        // super(icao24, firstSeen, lastSeen, departureAirport, arrivalAirport);
    }

    /**
     * Empty constructor needed for cloning
     */
    public EnrichedFlight () {
        setIcao24("");
        firstSeen = 0;
        lastSeen = 0;
        arrivalAirport = "";
        departureAirport = "";
        timeInAir = 0;
    }

    /**
     * Creates a flight object.
     * @param copiedFlight - flight with all needed values.
     */
    public EnrichedFlight(Flight copiedFlight) {
        setIcao24(copiedFlight.getIcao24());
        this.firstSeen = copiedFlight.getFirstSeen();
        this.lastSeen = copiedFlight.getLastSeen();
        this.departureAirport = copiedFlight.getDepartureAirport().getCode();
        this.arrivalAirport = copiedFlight.getArrivalAirport().getCode();
        this.timeInAir =0;

        updateTimeInAir(copiedFlight.getLastSeen(), copiedFlight.getFirstSeen());
    }

    /**
     * Sets the integer representing time in the air in seconds.
     * @param firstSeen - when aircraft was first seen during the flight.
     * @param lastSeen - when aircraft was last seen during the flight.
     */
    public void updateTimeInAir(int lastSeen, int firstSeen) {
        this.timeInAir = lastSeen - firstSeen;
    }

    /**
     * Class for comparing flights based on firstSeen timestamp, lastSeen timestamp and departure airport.
     */
    public static class FlightComparator implements Comparator<EnrichedFlight> {
        @Override
        public int compare(EnrichedFlight a, EnrichedFlight b) {
            int firstSeenCompare = a.firstSeen - b.firstSeen;
            int lastSeenCompare = a.lastSeen - b.lastSeen;
            int departureAirportCompare = a.departureAirport.compareTo(b.departureAirport);

            return (firstSeenCompare == 0)
                    ? (lastSeenCompare == 0)
                    ? departureAirportCompare
                    : lastSeenCompare
                    : firstSeenCompare;
        }
    }

    /**
     * Printing flights.
     * @return Flight with all fields in a string format.
     */
    @Override
    public String toString() {
        return "EnrichedFlight{" +
                "icao24='" + getIcao24() + "'" +
                ", firstSeen=" + firstSeen +
                ", lastSeen=" + lastSeen +
                ", departureAirport='" + departureAirport + "'" +
                ", arrivalAirport='" + arrivalAirport + "'" +
                ", timeInAir=" + timeInAir +
                "}";
    }

    /**
     * Clonning EnrichedFlights.
     */
    @Override
    public EnrichedFlight clone() {
        EnrichedFlight newFlight = new EnrichedFlight();
        newFlight.setIcao24(getIcao24());
        newFlight.setFirstSeen(firstSeen);
        newFlight.setLastSeen(lastSeen);
        newFlight.setDepartureAirport(departureAirport);
        newFlight.setArrivalAirport(arrivalAirport);
        newFlight.updateTimeInAir(lastSeen, firstSeen);
        return newFlight;    }

    /**
     * Equality based on icao24 and firstSeen.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrichedFlight flight = (EnrichedFlight) o;
        return firstSeen == flight.firstSeen && Objects.equals(getIcao24(), flight.getIcao24());
    }

    /**
     * Hash code based on icao24 and firstSeen.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIcao24(), firstSeen);
    }
}
