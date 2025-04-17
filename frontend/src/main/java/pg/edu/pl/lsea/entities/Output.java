package pg.edu.pl.lsea.entities;

/**
 * Class dedicated to storing output of function that analyze data
 * it stores then in format consisting of two parts, one is random ICAO of plane that is either of desired model of operator so desired model or operator can be identified and second part int value for that thing
 */
public class Output extends Trackable{

    public int Value;

    // TODO - Here probably should be many classes for different types of analysis with different, clear and understandable names

    public Output(String icao24, int Value) {
        setIcao24(icao24);
        this.Value = Value;
    }

    /**
     * @return gives in human-readable form information from object
     */
    @Override
    public String toString() {
        return "Aircraft{" +
                "icao24='" + getIcao24() + "'" +
                ", Value=" + Value +
                "}";
    }


}
