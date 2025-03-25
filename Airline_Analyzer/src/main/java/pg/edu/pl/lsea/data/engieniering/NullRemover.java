package pg.edu.pl.lsea.data.engieniering;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class NullRemover extends DataTransform {

    /**
     * Removes entieties with empty fields
     * @param aircrafts list of aircrafts that will be changed
     * @param flights list of flights that will be changed
     */
    public  void TransformAll (List<Aircraft> aircrafts, List<Flight> flights) {

        for(int i = aircrafts.size() - 1; i >= 0; i--){
            if(aircrafts.get(i).getIcao24() == null || aircrafts.get(i).getOwner() == null || aircrafts.get(i).getOperator() == null || aircrafts.get(i).getModel() == null)
                aircrafts.remove(i);
        }


        for(int i = aircrafts.size() - 1; i >= 0; i--){
            if(Objects.equals(aircrafts.get(i).getOwner(), "") || Objects.equals(aircrafts.get(i).getOperator(), "") || Objects.equals(aircrafts.get(i).getModel(), ""))
                aircrafts.remove(i);
        }

        for(int i = aircrafts.size() - 1; i >= 0; i--){
            if(Objects.equals(aircrafts.get(i).getOwner(), "NULL") || Objects.equals(aircrafts.get(i).getOperator(), "NULL") || Objects.equals(aircrafts.get(i).getModel(), "NULL"))
                aircrafts.remove(i);
        }

        for(int i = flights.size() - 1; i >= 0; i--){
            if(flights.get(i).getIcao24() == null || flights.get(i).getDepartureAirport() == null || flights.get(i).getArrivalAirport() == null || flights.get(i).getFirstSeen() == -1 || flights.get(i).getLastSeen() == -1)
                flights.remove(i);
        }

        for(int i = flights.size() - 1; i >= 0; i--){
            if(Objects.equals(flights.get(i).getIcao24(), "") || Objects.equals(flights.get(i).getDepartureAirport(), "") || Objects.equals(flights.get(i).getArrivalAirport(), ""))
                flights.remove(i);
        }

        for(int i = flights.size() - 1; i >= 0; i--){
            if(Objects.equals(flights.get(i).getIcao24(), "NULL") || Objects.equals(flights.get(i).getDepartureAirport(), "NULL") || Objects.equals(flights.get(i).getArrivalAirport(), "NULL"))
                flights.remove(i);
        }


    };
}
