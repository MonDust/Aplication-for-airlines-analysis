package pg.edu.pl.lsea.files;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.io.File;
import java.util.List;

public abstract class FileDataLoader {

    public abstract List<Aircraft> loadAircrafts(File file);
    public abstract List<Flight> loadFlights(File file);
}
