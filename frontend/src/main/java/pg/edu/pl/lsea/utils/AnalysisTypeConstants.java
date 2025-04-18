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
    public static final int PERFORM_ALL_TYPES = 1;
    public static final int MOST_POPULAR_OPERATORS = 2;
    public static final int MOST_POPULAR_MODELS = 3;
    public static final int PLOT_AVERAGE_TIME = 4;
    public static final int PERCENTAGE_OF_THE_LONG_FLIGHTS = 5;

    // Number of most popular operators to be shown at the end of analysis.
    public static final int NUMBER_OF_MOST_POPULAR_OPERATORS = 5;

    static {
        // Initialize the NAME_TO_ID map with strings mapped to the constants
        NAME_TO_ID = new LinkedHashMap<>();
        NAME_TO_ID.put("Perform all of types of analysis", PERFORM_ALL_TYPES);
        NAME_TO_ID.put("Show " + NUMBER_OF_MOST_POPULAR_OPERATORS + " most popular operators", MOST_POPULAR_OPERATORS);
        NAME_TO_ID.put("Show " + NUMBER_OF_MOST_POPULAR_OPERATORS + " most popular models", MOST_POPULAR_MODELS);
        NAME_TO_ID.put("Plot average time", PLOT_AVERAGE_TIME);
        NAME_TO_ID.put("Long flight percentage in top " + NUMBER_OF_MOST_POPULAR_OPERATORS + " operators" , PERCENTAGE_OF_THE_LONG_FLIGHTS);

        ID_TO_NAME = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : NAME_TO_ID.entrySet()) {
            ID_TO_NAME.put(entry.getValue(), entry.getKey());
        }
    }
}
