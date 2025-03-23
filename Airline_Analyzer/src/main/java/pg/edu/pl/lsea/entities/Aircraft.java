package pg.edu.pl.lsea.entities;

/**
 * A class representing an aircraft described by a specific model, operator and owner.
 */
public class Aircraft extends Trackable{
    private String model;
    private String operator;
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
}
