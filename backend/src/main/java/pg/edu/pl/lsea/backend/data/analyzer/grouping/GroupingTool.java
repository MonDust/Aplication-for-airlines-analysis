package pg.edu.pl.lsea.backend.data.analyzer.grouping;

import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.*;

/**
 * Tool enabling grouping flights, for example by their operator or model.
 * Available in multithreaded version.
 */
public class GroupingTool extends BaseGroupingAnalyzer {
    /**
     * Default constructor for the class.
     */
    public GroupingTool() {}

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
     * @param listOfListsAll original list of list contain all flights
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel(List<List<EnrichedFlight>> listOfListsAll) {
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for (List<EnrichedFlight> enrichedFlights : listOfListsAll) {
            boolean isLongFlight = false;

            //searches if there is any long flight in this model
            for (EnrichedFlight flight : enrichedFlights) {
                if (flight.getTimeInAir() >= MINIMAL_TIME_IN_AIR_FOR_LONG_FLIGHT_SECONDS) {
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
