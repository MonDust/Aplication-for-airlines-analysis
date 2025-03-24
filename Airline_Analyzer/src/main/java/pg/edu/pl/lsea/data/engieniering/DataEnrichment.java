package pg.edu.pl.lsea.data.engieniering;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataEnrichment extends DataTransform {
    public  void TransformAll (List<Aircraft> aircrafts, List<Flight> flights) {
        for (int i = aircrafts.size() - 1; i >= 0; i--) {
            if (Objects.equals(aircrafts.get(i).getOwner(), "")) {
                aircrafts.get(i).setOwner("EMPTY");
            }
            if (Objects.equals(aircrafts.get(i).getOperator(), "")) {
                aircrafts.get(i).setOperator("EMPTY");
            }
            if (Objects.equals(aircrafts.get(i).getModel(), "")) {
                aircrafts.get(i).setModel("EMPTY");
            }

        }

        for (int i = flights.size() - 1; i >= 0; i--) {
            if (Objects.equals(flights.get(i).getArrivalairport(), "")) {
                flights.get(i).setArrivalairport("EMPTY");
            }
            if (Objects.equals(flights.get(i).getDepartureairport(), "")) {
                flights.get(i).setDepartureairport("EMPTY");
            }


        }
    }
    public List<EnrichedFlight> CreateEnrichedListOfFlights (List<Flight> flights){

        List<EnrichedFlight> flightsNew = new ArrayList<>();

        for (Flight flight : flights) {
            flightsNew.add(new EnrichedFlight(
                    flight.getIcao24(),
                    flight.getFirstseen(),
                    flight.getLastseen(),
                    flight.getDepartureairport(),
                    flight.getArrivalairport()
            ));
        }

        return flightsNew;
    }
}
