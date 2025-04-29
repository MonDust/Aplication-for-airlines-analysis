package pg.edu.pl.lsea.backend.entities.analysisresult;

import lombok.Getter;
import lombok.Setter;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.entities.Trackable;

/**
 * A class representing an analysis result related to the aircraft model.
 * It stores a numerical value associated with a specific model and aircraft (identified by its ICAO24 code).
 *
 * NOT IN USE: additional class containing model, value and icao24 - for getting the result of the analysis.
 */
@Setter
@Getter
public class Model extends Trackable implements Cloneable {

    private int value;
    private String model;

    /**
     * Creates a model result object.
     * @param icao24 A string representing the 6-character hexadecimal ICAO24 code of the trackable entity.
     * @param value An integer representing the numerical value related to this model.
     * @param model A string representing the name of the aircraft model.
     */
    public Model(String icao24, int value, String model) {
        setIcao24(icao24);
        this.value = value;
        this.model = model;
    }

    /**
     * Empty constructor needed for cloning and serialization.
     */
    public Model() {
        setIcao24("");
        this.value = 0;
        this.model = "";
    }

    /**
     * Returns a string representation of the model object.
     * @return A string containing the ICAO24 code, model name, and value.
     */
    @Override
    public String toString() {
        return "icao24='" + getIcao24() + "'" +
                ", model='" + model + "'" +
                ", value='" + value + "'";
    }
}
