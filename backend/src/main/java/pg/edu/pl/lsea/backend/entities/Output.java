package pg.edu.pl.lsea.backend.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Class dedicated to storing output of function that analyze data
 * it stores then in format consisting of two parts, one is random ICAO of plane that is either of desired model of operator so desired model or operator can be identified and second part int value for that thing
 */
@Getter
@Setter
public class Output extends Trackable {
    /**
     * Value resulting from the analysis, that is connected to the icao24.
     */
    private int value;


    /**
     * Constructs an Output object.
     * @param icao24 A string representing the 6-character hexadecimal ICAO24 code of the aircraft.
     * @param Value An integer value representing the analysis result related to the model or operator.
     */
    public Output(String icao24, int value) {
        setIcao24(icao24);
        this.value = value;
    }

    /**
     * Empty constructor.
     */
    public Output() {
        setIcao24("");
        this.value = 0;
    }

    /**
     * Returns a string representation of the output object.
     * @return A human-readable string containing the ICAO24 code and the value.
     */
    @Override
    public String toString() {
        return "Analysis_output{" +
                "icao24='" + getIcao24() + "'" +
                ", Value=" + value +
                "}";
    }


}
