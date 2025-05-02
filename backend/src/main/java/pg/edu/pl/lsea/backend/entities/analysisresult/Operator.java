package pg.edu.pl.lsea.backend.entities.analysisresult;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.lsea.backend.entities.Trackable;

/**
 * A class representing an analysis result related to the aircraft operator.
 * It stores a numerical value associated with a specific operator and aircraft (identified by its ICAO24 code).
 *
 * NOT IN USE: additional class containing operator, value and icao24 - for getting the result of the analysis.
 */
@Setter
@Getter
public class Operator extends Trackable implements Cloneable {

    private int value;
    private String operator;

    /**
     * Creates an operator result object.
     * @param icao24 A string representing the 6-character hexadecimal ICAO24 code of the trackable entity.
     * @param value An integer representing the numerical value related to this operator.
     * @param operator A string representing the operator of the aircraft (e.g., an airline).
     */
    public Operator(String icao24, int value, String operator) {
        setIcao24(icao24);
        this.value = value;
        this.operator = operator;
    }

    /**
     * Empty constructor needed for cloning and serialization.
     */
    public Operator() {
        setIcao24("");
        this.value = 0;
        this.operator = "";
    }

    /**
     * Returns a string representation of the operator object.
     * @return A string containing the ICAO24 code, operator name, and value.
     */
    @Override
    public String toString() {
        return "icao24='" + getIcao24() + "'" +
                ", operator='" + operator + "'" +
                ", value='" + value + "'";
    }
}
