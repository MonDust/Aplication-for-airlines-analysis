package pg.edu.pl.lsea.backend.data.analyzer.grouping;

import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.*;

/**
 * Base abstract class with functions needed for grouping.
 */
public abstract class BaseGroupingAnalyzer {
    /** Time in seconds of flights that classify as long flights. */
    protected static final int MINIMAL_TIME_IN_AIR_FOR_LONG_FLIGHT_SECONDS = 1800;

    /**
     * Get all unique models from a list of aircraft.
     * @param aircrafts list of all aircraft (to find all types of models in)
     * @return list of unique models
     */
    protected List<String> getUniqueModels(List<Aircraft> aircrafts) {
        List<String> models = new ArrayList<>();
        Set<String> seenModels = new HashSet<>();

        // get list of unique models that are in records
        for (Aircraft aircraft : aircrafts) {
            String model = aircraft.getModel();
            if (model != null && seenModels.add(model)) {
                models.add(model);
            }
        }
        return models;
    }

    /**
     * Returns list of strings with contain
     * @param aircrafts list of all aircraft
     * @return  unique list of operator
     */
    protected List<String> getUniqueOperators(List<Aircraft> aircrafts) {
        List<String> operators = new ArrayList<>();
        Set<String> seenOperators = new HashSet<>();

        // get list of unique operators that are in records
        for (Aircraft aircraft : aircrafts) {
            String operator = aircraft.getOperator();
            if (operator != null && seenOperators.add(operator)) {
                operators.add(operator);
            }
        }
        return operators;
    }

    /**
     * Group flights per their model
     * @param flights list of all flights
     * @param aircrafts list of all aircraft
     * @param model desired model
     * @return list of flights of only that operator
     */
    protected List<EnrichedFlight> getAllFlightsForModel(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String model) {
        List<String> icaoList = new ArrayList<>();

        //find Icao24 of flights that interest us - have specific model
        for (Aircraft aircraft : aircrafts) {
            if (model.equals(aircraft.getModel())) {
                icaoList.add(aircraft.getIcao24());
            }
        }

        // get flights with desired Icao24s
        List<EnrichedFlight> result = new ArrayList<>();
        for (EnrichedFlight flight : flights) {
            if (icaoList.contains(flight.getIcao24())) {
                result.add(flight);
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
    protected List<EnrichedFlight> getAllFlightsForOperator(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String operator) {
        List<String> icaoList = new ArrayList<>();

        //find Icao24 of flights that interest us - have specific operator
        for (Aircraft aircraft : aircrafts) {
            if (operator.equals(aircraft.getOperator())) {
                icaoList.add(aircraft.getIcao24());
            }
        }

        // get flights with desired Icao24s
        List<EnrichedFlight> result = new ArrayList<>();
        for (EnrichedFlight flight : flights) {
            if (icaoList.contains(flight.getIcao24())) {
                result.add(flight);
            }
        }
        return result;
    }


}
