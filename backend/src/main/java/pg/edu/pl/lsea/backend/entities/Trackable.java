package pg.edu.pl.lsea.backend.entities;

/**
 * Abstract class representing trackable entities such as flight and aircraft.
 */
public abstract class Trackable implements Comparable<Trackable> {
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

    /**
     *  Abstract method for printing trackable objects.
     * @return Trackable object with all fields in a string format.
     */
    public abstract String toString();

    /**
     *  Compares trackable objects based on icao24.
     * @param other The object to be compared.
     * @return Negative integer if first object's icao24 is smaller,
     * positive integer if first object's icao24 is greater,
     * zero if both have the same icao24.
     */
    @Override
    public int compareTo(Trackable other) {
        return this.icao24.compareTo(other.icao24);
    }

}
