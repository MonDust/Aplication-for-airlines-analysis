package pg.edu.pl.lsea.data.engieniering;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class NullRemover extends DataTransform {


    public  void TransformAll (List<Aircraft> aircrafts, List<Flight> flights) {

        for(int i = aircrafts.size() - 1; i >= 0; i--){
            if(aircrafts.get(i).getIcao24() == null || aircrafts.get(i).getOwner() == null || aircrafts.get(i).getOperator() == null || aircrafts.get(i).getModel() == null)
                aircrafts.remove(i);
        }

        for(int i = aircrafts.size() - 1; i >= 0; i--){
            if(Objects.equals(aircrafts.get(i).getOwner(), "") || Objects.equals(aircrafts.get(i).getOperator(), "") || Objects.equals(aircrafts.get(i).getModel(), ""))
                aircrafts.remove(i);
        }

        for(int i = flights.size() - 1; i >= 0; i--){
            if(flights.get(i).getIcao24() == null || flights.get(i).getDepartureairport() == null || flights.get(i).getArrivalairport() == null || flights.get(i).getFirstseen() == -1 || flights.get(i).getLastseen() == -1)
                flights.remove(i);
        }

        for(int i = flights.size() - 1; i >= 0; i--){
            if(Objects.equals(flights.get(i).getIcao24(), "") || Objects.equals(flights.get(i).getDepartureairport(), "") || Objects.equals(flights.get(i).getArrivalairport(), ""))
                flights.remove(i);
        }


    };
}
