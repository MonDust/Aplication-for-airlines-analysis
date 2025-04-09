package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.*;

/**
 * Tool enabling grouping flights, for example by their operator or model
 */
public class GroupingTool {


    //Time in seconds of flights that classify as long flights
    private static final int minimalTimeInAirForLongFlightSeconds = 1800;


    /**
     * Group flights per their model
     * @param flights list of all flights
     * @param aircrafts list of all aircraft
     * @param model desired model
     * @return list of flights of only that operator
     */
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

    /**
     * Group flights per their operator
     * @param flights list of all flights
     * @param aircrafts list of all aircraft
     * @param operator desired operator
     * @return list of flights of only that operator
     */
    private List<EnrichedFlight> getAllFlightsForOperator(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String operator){
        List<EnrichedFlight> result = new ArrayList<>();
        List<String> icaoList = new ArrayList<>();

        //find ICAO of flights that intrest us
        for(Aircraft aircraft : aircrafts) {
            if(Objects.equals(aircraft.getOperator(), operator)){
                icaoList.add(aircraft.getIcao24());
            }
        }
        //get flights with desitred ICAO
        for (EnrichedFlight flight : flights) {
            for(String icao : icaoList) {
                if (Objects.equals(flight.getIcao24(), icao)) {
                    result.add(flight);
                }
            }
        }

        return result;
    }

    /**
     * Returns list of strings with contain
     * @param aircrafts list of all aircraft
     * @return unique list of models
     */
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

    /**
     * Returns list of strings with contain
     * @param aircrafts list of all aircraft
     * @return  unique list of operator
     */
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

    /**
     * Group list of all flights by models
     * @param flights list of all flights
     * @param aircrafts list of all aircraft
     * @return list of list where each list is list of flights which have the same model
     */
    public List<List<EnrichedFlight>> groupFlightsByModel (List<EnrichedFlight> flights, List<Aircraft> aircrafts) {
        List<String> models = getUniqueModels(aircrafts);
        List<List<EnrichedFlight>> result = new ArrayList<>();

        for(String model : models) {
            result.add(getAllFlightsForModel(flights, aircrafts, model));
        }

        return result;
    }
    /**
     * Group list of all flights by operator
     * @param flights list of all flights
     * @param aircrafts list of all aircraft
     * @return list of list where each list is list of flights which have the same operator
     */
    public List<List<EnrichedFlight>> groupFlightsByOperator (List<EnrichedFlight> flights, List<Aircraft> aircrafts) {
        List<String> operators = getUniqueOperators(aircrafts);
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for(String operator : operators) {
            output.add(getAllFlightsForOperator(flights, aircrafts, operator));
        }

        return output;
    }

    /**
     * returns list of list which is containing any long flights
     * @param input original list of list contain all flights
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel(List<List<EnrichedFlight>> input) {
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for (List<EnrichedFlight> enrichedFlights : input) {
            boolean isLongFlight = false;

            //searches if there is any long flight in this model
            for (EnrichedFlight flight : enrichedFlights) {
                if (flight.getTimeInAir() >= minimalTimeInAirForLongFlightSeconds) {
                    isLongFlight = true;
                    break;
                }
            }

            //if this model have any long flight add this flight to new list of lists
            if (isLongFlight) {
                output.add(enrichedFlights);
            }
        }
        return output;
    }
}
