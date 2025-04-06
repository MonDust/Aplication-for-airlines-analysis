package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.*;

public class GroupingTool {

    private List<EnrichedFlight> groupFlightsByModel(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String SearchedModel){

        List<EnrichedFlight> output = new ArrayList<>();

        List<String> icaoList = new ArrayList<>();



        for(Aircraft aircraft : aircrafts) {
            if(Objects.equals(aircraft.getModel(),SearchedModel)){
                icaoList.add(aircraft.getIcao24());

            }
        }


            for (EnrichedFlight flight : flights) {
                for(String icao : icaoList) {
                    if (Objects.equals(flight.getIcao24(), icao)) {
                        output.add(flight);

                    }
                }
            }

        return output;
    }

    private List<String> giveListOfModels (List<Aircraft> aircrafts){

        List<String> output = new ArrayList<>();
        Set<String> seenModels = new HashSet<>();

        for (Aircraft aircraft : aircrafts) {
            String model = aircraft.getModel();
            if (model != null && seenModels.add(model)) {
                output.add(model);
            }
        }

        return output;
    }

    public List<List<EnrichedFlight>> sortFlightsByModel (List<EnrichedFlight> flights, List<Aircraft> aircrafts) {


        List<String> listOfModels = giveListOfModels(aircrafts);
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for(String model : listOfModels) {
            output.add(groupFlightsByModel(flights, aircrafts, model));
        }



    return output;
    }

    public List<List<EnrichedFlight>> sortModelsWithLongCourses(List<List<EnrichedFlight>> input) {
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for (List<EnrichedFlight> enrichedFlights : input) {
            boolean allShortFlights = true;

            for (EnrichedFlight flight : enrichedFlights) {
                if (flight.getTimeInAir() >= 1800) {
                    allShortFlights = false;
                    break;
                }
            }

            if (!allShortFlights) {
                output.add(enrichedFlights);
            }
        }
        return output;
    }
}
