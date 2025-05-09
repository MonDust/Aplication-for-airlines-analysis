package pg.edu.pl.lsea.backend.data.engieniering;

import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.original.Flight;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.Operator;

import java.util.List;
import java.util.Objects;

/**
 * Class that is used to delete with null values
 */
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
     * Removes operators with empty fields
     * @param operators list of operators that will be cleaned
     */
    public void TransformOperators (List<Operator> operators) {
        for(int i = operators.size() - 1; i >= 0; i--){
            if(Objects.equals(operators.get(i).getName(), ""))
                operators.remove(i);
        }
    }

    /**
     * Removes models with empty fields
     * @param models list of operators that will be cleaned
     */
    public void TransformModels (List<Model> models) {
        for(int i = models.size() - 1; i >= 0; i--){
            if(Objects.equals(models.get(i).getName(), ""))
                models.remove(i);
        }
    }

    /**
     * Removes entities with empty fields
     * @param aircrafts list of aircrafts that will be changed
     */
    public void TransformAircrafts (List<Aircraft> aircrafts){
        for(int i = aircrafts.size() - 1; i >= 0; i--){
            if(aircrafts.get(i).getIcao24() == null || aircrafts.get(i).getOwner() == null || Objects.equals(aircrafts.get(i).getOperator().getName(), "") || Objects.equals(aircrafts.get(i).getModel().getName(), ""))
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

    /**
     * Function that checks whether there is null value in flight.
     * @param flight flight entity that is checked weather there are any nulls in it
     * @return true if there is no nulls in
     */
    public boolean CheckOneFlight (Flight flight) {
        if (flight.getIcao24() == null || flight.getRoute() == null || flight.getFirstSeen() == -1 || flight.getLastSeen() == -1){
            return false;
        }
        if (Objects.equals(flight.getIcao24(), "") || Objects.equals(flight.getRoute(), "")){
            return false;
        }
        return !Objects.equals(flight.getIcao24(), "NULL") && !Objects.equals(flight.getRoute(), "NULL");
    }
}
