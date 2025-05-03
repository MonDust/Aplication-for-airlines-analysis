package pg.edu.pl.lsea.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a tracked flight of an aircraft
 */
@Setter
@Getter
@Entity
@Table(
        name = "operators",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
public class Operator implements Cloneable, Comparable<Operator> {
    /**
     * This value is generated automatically during object creation
     * and is added by the JPA (Java Persistence API).
     */
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Aircraft> aircrafts = new ArrayList<>();

    /**
     * Creates a flight object.
     */
    public Operator(String name) {
        this.name = name;
    }

    /**
     * Empty constructor needed for cloning
     */
    public Operator() {
        this.name = "";
    }

    /**
     * Class for comparing operators based on aircrafts size.
     */
    @Override
    public int compareTo(Operator o) {
        return o.aircrafts.size() - aircrafts.size();
    }


    /**
     * Printing operator.
     * @return Operator with all fields in a string format.
     */
    @Override
    public String toString() {
        return "Operator{" +
               "name='" + name + "'" +
               ", aircrafts.size() =" + aircrafts.size() +
               "}";
    }



    @Override
    public Operator clone() {
        Operator newFlight = new Operator();
        newFlight.setId(id);
        newFlight.setName(name);
        newFlight.setAircrafts(aircrafts);
        return newFlight;
    }

    /**
     * A method that compares operator to another object and determines if they are equal based on its name.
     * @param o the objects to compare the operator to
     * @return true if the compared objects are considered equal, false if they are different
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operator operator = (Operator) o;
        return Objects.equals(name, operator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

