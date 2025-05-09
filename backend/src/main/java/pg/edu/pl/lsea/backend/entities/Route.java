package pg.edu.pl.lsea.backend.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import pg.edu.pl.lsea.backend.entities.original.Flight;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(
        name = "routes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"origin_id", "destination_id"})
)
public class Route implements Cloneable, Comparable<Route>{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origin_id", referencedColumnName = "id", nullable = false)
    private Airport origin;

    @ManyToOne
    @JoinColumn(name = "destination_id", referencedColumnName = "id", nullable = false)
    private Airport destination;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Flight> flights = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "route_operators",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "operator_id")
    )
    private Set<Operator> operators = new HashSet<>();

    public Route() {
        this.origin = new Airport();
        this.destination = new Airport();
    }

    public Route(Airport origin, Airport destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public void addOperator(Operator operator) {
        operators.add(operator);
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public void bulkAddFlights(List<Flight> flights) {
        flights.forEach(this::addFlight);
    }

    public void bulkAddOperators(List<Operator> operators) {
        operators.forEach(this::addOperator);
    }

    public void deleteFlight(Flight flight) {
        flights.remove(flight);
    }

    public void deleteOperator(Operator operator) {
        operators.remove(operator);
    }

    public void bulkDeleteFlights(List<Flight> flights) {
        flights.forEach(this::deleteFlight);
    }

    public void bulkDeleteOperators(List<Operator> operators) {
        operators.forEach(this::deleteOperator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(origin, route.origin) &&
                Objects.equals(destination, route.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", origin=" + (origin != null ? origin.getCode() : "null") +
                ", destination=" + (destination != null ? destination.getCode() : "null") +
                ", flightsCount=" + flights.size() +
                ", operatorsCount=" + operators.size() +
                '}';
    }

    @Override
    public Route clone() {
        Route cloned = new Route();
        cloned.setOrigin(this.origin);
        cloned.setDestination(this.destination);
        cloned.setFlights(new HashSet<>(this.flights));
        cloned.setOperators(new HashSet<>(this.operators));
        return cloned;
    }

    @Override
    public int compareTo(Route other) {
        int originCompare = this.origin.getCode().compareTo(other.origin.getCode());
        if (originCompare != 0) return originCompare;
        return this.destination.getCode().compareTo(other.destination.getCode());
    }

}
