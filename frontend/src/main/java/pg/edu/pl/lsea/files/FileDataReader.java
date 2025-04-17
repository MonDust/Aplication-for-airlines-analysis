package pg.edu.pl.lsea.files;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.io.File;
import java.util.List;

/**
 * Abstract class representing different file readers.
 * It has methods responsible for reading flights and aircrafts.
 */
public abstract class FileDataReader {
    /**
     *
     * @param aircraftsFile File with aircrafts which should be read
     * @return The list of aircrafts that could be read from the file
     */
    public abstract List<Aircraft> readAircrafts(File aircraftsFile);

    /**
     *
     * @param flightsFile File with flights which should be read
     * @return The list of flights that could be read from the file
     */
    public abstract List<Flight> readFlights(File flightsFile);
}
