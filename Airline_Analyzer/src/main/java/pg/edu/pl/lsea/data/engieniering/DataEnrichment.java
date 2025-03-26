package pg.edu.pl.lsea.data.engieniering;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class responsible for first draft of data mining
 */
public class DataEnrichment extends DataTransform {

    /**
     * Fills empty fields with "EMPTY" value
     * @param aircrafts list of aircrafts that will be changed
     * @param flights list of flights that will be changed
     */
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
            if (Objects.equals(flights.get(i).getArrivalAirport(), "")) {
                flights.get(i).setArrivalAirport("EMPTY");
            }
            if (Objects.equals(flights.get(i).getDepartureAirport(), "")) {
                flights.get(i).setDepartureAirport("EMPTY");
            }


        }
    }

    /**
     * Uses deep clone to create list of EnrichedFlight
     * @param flights list of flights that will be changed
     * @return list of flights witch was enriched by deep clone into entities EnrichedFlight
     */
    public List<EnrichedFlight> CreateEnrichedListOfFlights (List<Flight> flights){

        List<EnrichedFlight> flightsNew = new ArrayList<>();

        for (Flight flight : flights) {
            flightsNew.add(new EnrichedFlight(
                    flight.getIcao24(),
                    flight.getFirstSeen(),
                    flight.getLastSeen(),
                    flight.getDepartureAirport(),
                    flight.getArrivalAirport()
            ));
        }

        return flightsNew;
    }
}
