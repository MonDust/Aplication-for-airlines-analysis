package pg.edu.pl.lsea.entities;

/**
 * Abstract class representing trackable entities such as flight and aircraft.
 */
public abstract class Trackable {
    /**
     * Unique icao24 code for identifying aircraft using a 24-bit 6-character hexadecimal format.
     */
    private String icao24;

    /**
     * Returns the icao24 code.
     * @return A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     */
    public String getIcao24() {
        return icao24;
    }

    /**
     * Sets the icao24 code.
     * @param icao24 A string representing the 6-character hexadecimal icao24 code of the trackable entity.
     */
    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }
}
