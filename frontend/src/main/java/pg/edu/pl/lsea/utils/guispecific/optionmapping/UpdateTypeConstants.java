package pg.edu.pl.lsea.utils.guispecific.optionmapping;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Constants for updating/deleting information.
 */
public class UpdateTypeConstants {

    // Maps for update-to-id and id-to-update lookup
    public static final Map<String, Integer> UPDATE_TO_ID;
    public static final Map<Integer, String> ID_TO_UPDATE;

    // Constants for each update type
    public static final int UPDATE_A = 1;
    public static final int UPDATE_F = 2;
    public static final int DELETE_A = 3;
    public static final int DELETE_F = 4;

    static {
        // Initialize the INFO_NAME_TO_ID map with strings mapped to the constants
        UPDATE_TO_ID = new LinkedHashMap<>();
        UPDATE_TO_ID.put("Update Aircraft", UPDATE_A);
        UPDATE_TO_ID.put("Update Flight", UPDATE_F);
        UPDATE_TO_ID.put("Delete Aircrafts", DELETE_A);
        UPDATE_TO_ID.put("Delete Flights", DELETE_F);

        ID_TO_UPDATE = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : UPDATE_TO_ID.entrySet()) {
            ID_TO_UPDATE.put(entry.getValue(), entry.getKey());
        }
    }
}

