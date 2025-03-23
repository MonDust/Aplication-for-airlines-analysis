package pg.edu.pl.lsea.files;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.io.File;
import java.util.List;

/**
 * Abstract class representing different file loaders.
 * It has methods responsible for loading flights and aircrafts.
 */
public abstract class FileDataLoader {
    /**
     *
     * @param file File with aircrafts which should be read
     * @return The list of aircrafts that could be loaded from the file
     */
    public abstract List<Aircraft> loadAircrafts(File file);

    /**
     *
     * @param file File with flights which should be read
     * @return The list of flights that could be loaded from the file
     */
    public abstract List<Flight> loadFlights(File file);
}
