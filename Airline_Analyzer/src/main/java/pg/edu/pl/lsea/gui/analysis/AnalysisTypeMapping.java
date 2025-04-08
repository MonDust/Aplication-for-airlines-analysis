package pg.edu.pl.lsea.gui.analysis;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalysisTypeMapping {

    public static final Map<String, Integer> NAME_TO_ID;
    public static final Map<Integer, String> ID_TO_NAME;

    static {
        NAME_TO_ID = new LinkedHashMap<>();
        NAME_TO_ID.put("Sequential vs Parallel performance", 1);
        NAME_TO_ID.put("Sort by number of flights", 2);
        NAME_TO_ID.put("Sort by total time of flights", 3);
        NAME_TO_ID.put("Correlation analysis", 4);
        NAME_TO_ID.put("Flights per aircraft table", 5);
        NAME_TO_ID.put("Group by operator", 6);
        NAME_TO_ID.put("Flights per airport", 7);
        NAME_TO_ID.put("Most used operators", 8);

        ID_TO_NAME = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : NAME_TO_ID.entrySet()) {
            ID_TO_NAME.put(entry.getValue(), entry.getKey());
        }
    }
}
