package pg.edu.pl.lsea.data.engieniering;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public abstract class DataTransform {


    public abstract void TransformAll (List<Aircraft> aircrafts, List<Flight> flights);

}
