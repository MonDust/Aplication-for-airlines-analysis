package pg.edu.pl.lsea.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a tracked airport of a flight
 */
@Setter
@Getter
@Entity
@Table(
        name = "airports",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"})
)
public class Airport implements Cloneable, Comparable<Airport> {
    /**
     * This value is generated automatically during object creation
     * and is added by the JPA (Java Persistence API).
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
    * IATA code of the airport.
    */
    @Column(name = "code", nullable = false)
    private String code;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Route> originRouteList = new ArrayList<>();

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Route> destinationRouteList = new ArrayList<>();

//    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private List<Flight> departureFlights = new ArrayList<>();
//
//    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private List<Flight> arrivalFlights = new ArrayList<>();

//    public void addArrivalFlight(Flight flight) {
//        arrivalFlights.add(flight);
//    }
//
//    public void addDepartureFlight(Flight flight) {
//        departureFlights.add(flight);
//    }
//
//    public void bulkAddDepartureFlights(List<Flight> flights) {
//        departureFlights.addAll(flights);
//    }
//
//    public void bulkAddArrivalFlights(List<Flight> flights) {
//        arrivalFlights.addAll(flights);
//    }

    /**
     * Creates an airport object.
     */
    public Airport(String code) {
        this.code = code;
    }

    /**
     * Empty constructor needed for cloning
     */
    public Airport() {
        this.code = "";
    }

    /**
     * Class for comparing airports based on flights size.
     */
    @Override
    public int compareTo(Airport o) {
        return o.originRouteList.size() + o.destinationRouteList.size() - originRouteList.size() - destinationRouteList.size();
    }


    /**
     * Printing airport.
     * @return Model with all fields in a string format.
     */
    @Override
    public String toString() {
        return "Model{" +
                "code='" + code + "'" +
                ", departureFlights.size() =" + originRouteList.size() +
                ", arrivalFlights.size() =" + destinationRouteList.size() +
                "}";
    }



    @Override
    public Airport clone() {
        Airport newAirport = new Airport();
        newAirport.setId(id);
        newAirport.setCode(code);
        newAirport.setDestinationRouteList(destinationRouteList);
        newAirport.setOriginRouteList(originRouteList);
        return newAirport;
    }

    /**
     * A method that compares airport to another object and determines if they are equal based on its name.
     * @param o the objects to compare the airport to
     * @return true if the compared objects are considered equal, false if they are different
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airport airport = (Airport) o;
        return Objects.equals(code, airport.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

