package pg.edu.pl.lsea.backend.analysis.typesofanalysis;

import pg.edu.pl.lsea.backend.analysis.BaseAnalysis;
import pg.edu.pl.lsea.backend.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.List;

/**
 * Class responsible for the thread analysis.
 */
public class MultiThreadAnalysis extends BaseAnalysis {
    // message log that will be displayed at the end
    protected final StringBuilder messageLog = new StringBuilder();

    /**
     * Constructor of the class.
     */
    public MultiThreadAnalysis() {}

    /**
     * This function performs analysis without writing anything to output.
     * It's meant to be used for using multiple times and measuring its execution time
     * @param threads number of threads that should be used in the intensive part of the analysis
     * @param enrichedFlights list of flights
     * @param aircrafts list of aircraft
     */
    public void performAnalysis(int threads, List<EnrichedFlight> enrichedFlights, List<Aircraft> aircrafts) {
        // Grouping flights by model and operator using parallel processing
        List<List<EnrichedFlight>> groupedByModel = parallelGroupingTool.groupFlightsByModel(enrichedFlights, aircrafts, threads);
        List<List<EnrichedFlight>> groupedByOperator = parallelGroupingTool.groupFlightsByOperator(enrichedFlights, aircrafts, threads);

        // Sorting and analyzing
        SortingCalculator sortingCalculator = new SortingCalculator();
        sortingCalculator.giveTopNOperators(groupedByOperator, 10);
        sortingCalculator.sortByAmountOfFlights(enrichedFlights);
        sortingCalculator.sortByTimeOfFlights(enrichedFlights);

        // Analyzing long flights and calculating properties
        GroupingTool groupingTool = new GroupingTool();
        groupingTool.findLongFlightsForEachModel(groupedByModel);

        PropertiesCalculator propertiesCalculator = new PropertiesCalculator();
        propertiesCalculator.printAllAverages(groupedByModel);
        propertiesCalculator.givePercentageOfLongFlights(groupedByModel);
    }

    /**
     * Function for running the thread analysis.
     * @param parallel - true (is parallel) or false (is sequential)
     * @param threads - number of threads (will work only if parallel)
     */
    public void runAnalysis(boolean parallel, int threads) {
        long pre_analysis_start = System.currentTimeMillis();

        log("Starting analysis...");
        log(parallel ? "Parallel analysis selected." : "Sequential (single-threaded) analysis selected.");

        List<EnrichedFlight> enrichedFlights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        long pre_analysis_end = System.currentTimeMillis();
        long pre_analysis_duration = pre_analysis_end - pre_analysis_start;
        log("Pre-analysis duration (getting data ready - date engineering): " + pre_analysis_duration);

        int num_of_points = 5;
        int interval_flights = enrichedFlights.size() / num_of_points;

        long start_iteration, end_iteration;

        for (int i = 0; i < num_of_points; i++) {
            // "to" variable means how many elements from the array should we take into the analysis
            int to = (i == num_of_points - 1) ? enrichedFlights.size() : (i + 1) * interval_flights;

            List<EnrichedFlight> subList = enrichedFlights.subList(0, to);

            start_iteration = System.currentTimeMillis();
            performAnalysis(threads, subList, aircrafts);
            end_iteration = System.currentTimeMillis();
            log("threads: " + threads + ", i: " + i + ", number of elements: " + to + ", time: " + (end_iteration - start_iteration));
        }

        log("flights total size: " + enrichedFlights.size());
        log("aircrafts total size: " + aircrafts.size());

        log("Analysis complete.");
    }

    /**
     * Group by model with use of threads.
     * @param flights - list of enriched flights
     * @param aircrafts - list aircrafts
     * @param parallel - true (parallel) or false (sequential)
     * @param threads - number of threads
     * @return list of lists of enriched flights
     */
    private List<List<EnrichedFlight>> groupByModel(List<EnrichedFlight> flights, List<Aircraft> aircrafts, boolean parallel, int threads) {
        if (parallel) {
            log("Running parallel grouping by model using " + threads + " threads...");
            ParallelGroupingTool tool = new ParallelGroupingTool();
            return tool.groupFlightsByModel(flights, aircrafts, threads);
        } else {
            log("Running single-threaded grouping by model...");
            GroupingTool tool = new GroupingTool();
            return tool.groupFlightsByModel(flights, aircrafts);
        }
    }

    /**
     * Group by operator with use of threads.
     * @param flights - list of enriched flights
     * @param aircrafts - list aircrafts
     * @param parallel - true (parallel) or false (sequential)
     * @param threads - number of threads
     * @return list of lists of enriched flights
     */
    private List<List<EnrichedFlight>> groupByOperator(List<EnrichedFlight> flights, List<Aircraft> aircrafts, boolean parallel, int threads) {
        if (parallel) {
            log("Running parallel grouping by operator using " + threads + " threads...");
            ParallelGroupingTool tool = new ParallelGroupingTool();
            return tool.groupFlightsByOperator(flights, aircrafts, threads);
        } else {
            log("Running single-threaded grouping by operator...");
            GroupingTool tool = new GroupingTool();
            return tool.groupFlightsByOperator(flights, aircrafts);
        }
    }

    /**
     * Print averages for grouped data
     * @param groupedFlights - grouped flights (list of lists of enriched flights)
     * @param groupingType - what type of grouping happened
     */
    private void analyzeGroupedData(List<List<EnrichedFlight>> groupedFlights, String groupingType) {
        log("Analyzing properties for " + groupingType + "...");
        PropertiesCalculator propertiesCalculator = new PropertiesCalculator();
        propertiesCalculator.printAllAverages(groupedFlights);
        log("Finished analyzing " + groupingType + ".");
    }

    /**
     * Append a message to the log and optionally display it.
     * @param msg message to log
     */
    protected void log(String msg) {
        messageLog.append(msg).append("\n");

        // TODO - Update real time -> here should be sending the logs: sending 'msg'
    }

    /**
     * Returns the message log.
     * @return log
     */
    public String getMessages() {
        return messageLog.toString();
    }

}
