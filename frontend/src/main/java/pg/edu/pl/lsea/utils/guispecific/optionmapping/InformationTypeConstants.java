package pg.edu.pl.lsea.utils.guispecific.optionmapping;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Constants for information showing.
 */
public class InformationTypeConstants {

    // Maps for info-name-to-id and id-to-info-name lookup
    public static final Map<String, Integer> INFO_NAME_TO_ID;
    public static final Map<Integer, String> ID_TO_INFO_NAME;

    // Constants for each analysis type
    public static final int RECORDS_SIZE = 1;
    public static final int FLIGHTS_INFORMATION = 2;
    public static final int AIRCRAFTS_INFORMATION = 3;

    static {
        // Initialize the INFO_NAME_TO_ID map with strings mapped to the constants
        INFO_NAME_TO_ID = new LinkedHashMap<>();
        INFO_NAME_TO_ID.put("Size of data loaded", RECORDS_SIZE);
        INFO_NAME_TO_ID.put("Show records - flight", FLIGHTS_INFORMATION);
        INFO_NAME_TO_ID.put("Show records - aircraft", AIRCRAFTS_INFORMATION);

        ID_TO_INFO_NAME = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : INFO_NAME_TO_ID.entrySet()) {
            ID_TO_INFO_NAME.put(entry.getValue(), entry.getKey());
        }
    }
}
