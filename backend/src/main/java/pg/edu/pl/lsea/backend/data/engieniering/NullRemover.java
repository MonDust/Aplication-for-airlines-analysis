package pg.edu.pl.lsea.backend.data.engieniering;

import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.Flight;

import java.util.List;
import java.util.Objects;

public class NullRemover extends DataTransform {

    /**
     * Removes entities with empty fields
     * @param flights list of flights that will be changed
     */
    public void TransformFlights (List<Flight> flights){

        for(int i = flights.size() - 1; i >= 0; i--){
            if(!CheckOneFlight(flights.get(i)))
                flights.remove(i);
        }

    }

    /**
     * Removes entities with empty fields
     * @param aircrafts list of aircrafts that will be changed
     */
    public  void TransformAircrafts (List<Aircraft> aircrafts){
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

    }


    public boolean CheckOneFlight (Flight flight) {
        if (flight.getIcao24() == null || flight.getDepartureAirport() == null || flight.getArrivalAirport() == null || flight.getFirstSeen() == -1 || flight.getLastSeen() == -1){
            return false;
}
        if (Objects.equals(flight.getIcao24(), "") || Objects.equals(flight.getDepartureAirport(), "") || Objects.equals(flight.getArrivalAirport(), "")){
            return false;
        }

        if(Objects.equals(flight.getIcao24(), "NULL") || Objects.equals(flight.getDepartureAirport(), "NULL") || Objects.equals(flight.getArrivalAirport(), "NULL")) {
                return false;
            }

        return true;
    }

}
