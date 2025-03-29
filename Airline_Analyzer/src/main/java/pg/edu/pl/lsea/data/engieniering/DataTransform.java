package pg.edu.pl.lsea.data.engieniering;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

/**
 * Abstract class responsible for all transformation connected with turning data transfer object into object that can be more easly analized
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
