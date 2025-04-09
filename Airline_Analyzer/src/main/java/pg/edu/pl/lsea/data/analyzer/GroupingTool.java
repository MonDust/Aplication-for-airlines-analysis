package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.*;

public class GroupingTool {

    private static final int minimalTimeInAirForLongFlightSeconds = 1800;


    private List<EnrichedFlight> getAllFlightsForModel(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String model){
        List<EnrichedFlight> result = new ArrayList<>();
        List<String> icaoList = new ArrayList<>();

        for(Aircraft aircraft : aircrafts) {
            if(Objects.equals(aircraft.getModel(), model)){
                icaoList.add(aircraft.getIcao24());
            }
        }
        for (EnrichedFlight flight : flights) {
            for(String icao : icaoList) {
                if (Objects.equals(flight.getIcao24(), icao)) {
                    result.add(flight);
                }
            }
        }
        return result;
    }


    private List<EnrichedFlight> getAllFlightsForOperator(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String operator){
        List<EnrichedFlight> result = new ArrayList<>();
        List<String> icaoList = new ArrayList<>();

        for(Aircraft aircraft : aircrafts) {
            if(Objects.equals(aircraft.getOperator(), operator)){
                icaoList.add(aircraft.getIcao24());
            }
        }
        for (EnrichedFlight flight : flights) {
            for(String icao : icaoList) {
                if (Objects.equals(flight.getIcao24(), icao)) {
                    result.add(flight);
                }
            }
        }

        return result;
    }

    private List<String> getUniqueModels (List<Aircraft> aircrafts){

        List<String> result = new ArrayList<>();
        Set<String> seenModels = new HashSet<>();

        for (Aircraft aircraft : aircrafts) {
            String model = aircraft.getModel();
            if (model != null && seenModels.add(model)) {
                result.add(model);
            }
        }

        return result;
    }


    private List<String> getUniqueOperators (List<Aircraft> aircrafts){

        List<String> result = new ArrayList<>();
        Set<String> seenOperators = new HashSet<>();

        for (Aircraft aircraft : aircrafts) {
            String operator = aircraft.getOperator();
            if (operator != null && seenOperators.add(operator)) {
                result.add(operator);
            }
        }

        return result;
    }


    public List<List<EnrichedFlight>> groupFlightsByModel (List<EnrichedFlight> flights, List<Aircraft> aircrafts) {
        List<String> models = getUniqueModels(aircrafts);
        List<List<EnrichedFlight>> result = new ArrayList<>();

        for(String model : models) {
            result.add(getAllFlightsForModel(flights, aircrafts, model));
        }

        return result;
    }

    public List<List<EnrichedFlight>> groupFlightsByOperator (List<EnrichedFlight> flights, List<Aircraft> aircrafts) {
        List<String> operators = getUniqueOperators(aircrafts);
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for(String operator : operators) {
            output.add(getAllFlightsForOperator(flights, aircrafts, operator));
        }

        return output;
    }

    public List<List<EnrichedFlight>> findLongFlightsForEachModel(List<List<EnrichedFlight>> input) {
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for (List<EnrichedFlight> enrichedFlights : input) {
            boolean isLongFlight = false;

            for (EnrichedFlight flight : enrichedFlights) {
                if (flight.getTimeInAir() >= minimalTimeInAirForLongFlightSeconds) {
                    isLongFlight = true;
                    break;
                }
            }

            if (isLongFlight) {
                output.add(enrichedFlights);
            }
        }
        return output;
    }
}
