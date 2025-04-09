package pg.edu.pl.lsea.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Constants for analysis.
 */
public class AnalysisTypeConstants {

    // Maps for name-to-id and id-to-name lookup
    public static final Map<String, Integer> NAME_TO_ID;
    public static final Map<Integer, String> ID_TO_NAME;

    // Constants for each analysis type
    public static final int THREADS_ANALYSIS = 1;
    public static final int SORT_BY_NUMBER_OF_FLIGHTS = 2;
    public static final int SORT_BY_TOTAL_TIME_OF_FLIGHTS = 3;
    public static final int MOST_POPULAR_OPERATORS = 4;
    // public static final int FLIGHTS_PER_AIRCRAFT_TABLE = 5;
    public static final int MOST_POPULAR_MODELS = 5;
    public static final int PLOT_AVERAGE_TIME = 6;
    // public static final int FLIGHTS_PER_AIRPORT = 7;
    public static final int FLIGHTS_PER_AIRPORT = 7;

    // Number of most popular models to be shown at the end of analysis.
    public static final int NUMBER_OF_MOST_POPULAR_OPERATORS = 5;

    static {
        // Initialize the NAME_TO_ID map with strings mapped to the constants
        NAME_TO_ID = new LinkedHashMap<>();
        NAME_TO_ID.put("Sequential vs Parallel performance", THREADS_ANALYSIS);
        NAME_TO_ID.put("Sort by number of flights", SORT_BY_NUMBER_OF_FLIGHTS);
        NAME_TO_ID.put("Sort by total time of flights", SORT_BY_TOTAL_TIME_OF_FLIGHTS);
        NAME_TO_ID.put("Show " + NUMBER_OF_MOST_POPULAR_OPERATORS + " most popular operators", MOST_POPULAR_OPERATORS);
        NAME_TO_ID.put("Show " + NUMBER_OF_MOST_POPULAR_OPERATORS + " most popular models", MOST_POPULAR_MODELS);
        NAME_TO_ID.put("Plot average time", PLOT_AVERAGE_TIME);
        NAME_TO_ID.put("Flights per airport", FLIGHTS_PER_AIRPORT);

        ID_TO_NAME = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : NAME_TO_ID.entrySet()) {
            ID_TO_NAME.put(entry.getValue(), entry.getKey());
        }
    }
}
