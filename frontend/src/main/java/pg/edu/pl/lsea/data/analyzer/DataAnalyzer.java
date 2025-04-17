package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;

import java.io.File;
import java.util.List;

/**
 * Abstract class representing different tools to analyze data
 */
public abstract class DataAnalyzer {
    /**
     * One function that provied data for dashbord
     * @param aircrafts list of aircraft meant to be analyzed
     * @param flights list of flights meant to be analyzed
     */
    public abstract void analyzeDataForDashbord (List<Aircraft> aircrafts, List<EnrichedFlight> flights);

}
