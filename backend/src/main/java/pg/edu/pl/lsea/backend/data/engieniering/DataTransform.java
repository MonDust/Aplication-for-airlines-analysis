package pg.edu.pl.lsea.backend.data.engieniering;

import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.original.Flight;

import java.util.List;

/**
 * Abstract class responsible for all transformation connected with turning data transfer object into object that can be more easily analized
 */
public abstract class DataTransform {

    /**
     * Function that will transform whole provided class
     * @param flights list of flights that will be changed
     */
    public abstract void TransformFlights (List<Flight> flights);


    /**
     * Function that will transform all provided classes
     * @param aircrafts list of aircrafts that will be changed
     */
    public abstract void TransformAircrafts (List<Aircraft> aircrafts);


}
