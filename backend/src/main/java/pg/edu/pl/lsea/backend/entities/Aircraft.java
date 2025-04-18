package pg.edu.pl.lsea.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Comparator;
import java.util.Objects;

/**
 * A class representing an aircraft described by a specific model, operator and owner.
 */
@Entity
@Table(name = "aircrafts")
public class Aircraft extends Trackable implements Cloneable{
    @Column(name = "model")
    private String model;

    @Column(name = "operator")
    private String operator;

    @Column(name = "owner")
    private String owner;

    /**
     * Creates an aircraft object.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     * @param model A string representing the model of the aircraft
     * @param operator A string representing the operator of the aircraft (for example an airline company)
     * @param owner A string representing the owner of the aircraft
     */
    public Aircraft(String icao24, String model, String operator, String owner) {
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
        this.model = "";
        this.operator = "";
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
     * Returns the model of the aircraft.
     * @return A string representing the model of the aircraft
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the operator of the aircraft.
     * @return A string representing the operator of the aircraft
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the owner of the aircraft.
     * @return A string representing the owner of the aircraft
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the model of the aircraft
     * @param model A string representing the model of the aircraft
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Sets the operator of the aircraft
     * @param operator A string representing the operator of the aircraft
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Sets the owner of the aircraft
     * @param owner A string representing the owner of the aircraft
     */
    public void setOwner(String owner) {
        this.owner = owner;
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
