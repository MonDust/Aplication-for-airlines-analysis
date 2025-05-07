package pg.edu.pl.lsea.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A class representing an aircraft described by a specific model, operator and owner.
 */
@Setter
@Getter
@Entity
@Table(
        name = "aircrafts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"icao24"}),
        indexes = @Index(name = "idx_icao24", columnList = "icao24")
)
public class Aircraft extends Trackable implements Cloneable{
    /**
     * This value is generated automatically during object creation
     * and is added by the JPA (Java Persistence API).
     */
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;
    /**
     * Operator of the aircraft.
     */
//    @Column(name = "operator")
//    private String operator;

    @ManyToOne()
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private Operator operator;
    /**
     * Owner of the aircraft.
     */
    @Column(name = "owner")
    private String owner;



    /**
     * One-to-many relation with flights: One aircraft can have many flights
     * Uses Cascade deletion: Deleting the Aircraft will also delete all associated Flights.
     * Orphan removal: If you remove a Flight from aircraft.getFlights(), it will also be
     * deleted from the database, not just disassociated.
     */
    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Flight> flights = new ArrayList<>();

    /**
     * Creates an aircraft object.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     * @param model A string representing the model of the aircraft
     * @param operator A string representing the operator of the aircraft (for example an airline company)
     * @param owner A string representing the owner of the aircraft
     */
    public Aircraft(String icao24, Model model, Operator operator, String owner) {
        setIcao24(icao24);
        this.model = model;
        this.operator = operator;
        this.owner = owner;
    }

    /**
     * Empty constructor needed for cloning
     */
    public Aircraft() {
        setIcao24("");
        this.model = null;
        this.operator = null;
        this.owner = "";
    }

    /**
     * Class for comparing aircrafts based on model, operator and owner.
     */
    public static class AircraftComparator implements Comparator<Aircraft> {
        /**
         * Compares aircrafts by model,
         * if model is the same compares by operator,
         * if operator is the same compares by owner.
         * @param a The first aircraft to be compared.
         * @param b The second aircraft to be compared.
         * @return Negative integer if first object is smaller by comparison of model, operator and owner,
         * positive integer if first object is greater,
         * zero if both have the same model, operator and owner.
         */
        @Override
        public int compare(Aircraft a, Aircraft b) {
            int modelCompare = a.model.compareTo(b.model);
            int operatorCompare = a.operator.compareTo(b.operator);
            int ownerCompare = a.owner.compareTo(b.owner);

            return (modelCompare == 0)
                    ? (operatorCompare == 0)
                    ? ownerCompare
                    : operatorCompare
                    : modelCompare;
        }
    }

    /**
     * Printing aircrafts
     * @return Aircraft with all fields in a string format.
     */
    @Override
    public String toString() {
        return "Aircraft{" +
                "icao24='" + getIcao24() + "'" +
                ", model='" + model + "'" +
                ", operator='" + operator + "'" +
                ", owner='" + owner + "'" +
                "}";
    }
    @Override
    public Aircraft clone() {
        Aircraft newAircraft = new Aircraft();
        newAircraft.setIcao24(getIcao24());
        newAircraft.setModel(model);
        newAircraft.setOperator(operator);
        newAircraft.setOwner(owner);
        return newAircraft;
    }
    /**
     * A method that compares an aircraft object to another object and determines if they are equal based on icao24 and model values.
     * @param o the objects to compare the aircraft to
     * @return true if the compared objects are considered equal, false if they are different
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        // checking aircraft uniqueness by model and icao24
        return Objects.equals(getIcao24(), aircraft.getIcao24()) && Objects.equals(model, aircraft.model);
    }
    /**
     * Calculates a hash code for aircraft objects based on icao24 and model values to ensure objects
     * with the same values of these fields are considered equal and have the same hash code
     * @return hash code value for the aircraft object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIcao24(), model);
    }

}
