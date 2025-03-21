package pg.edu.pl.lsea.files;

import java.io.File;  // Import the File class

public abstract class FileDataLoader {
    // FIX - Change return type to class of aircraft / flight
    public abstract void loadAircrafts(File file);
    public abstract void loadFlights(File file);
}
