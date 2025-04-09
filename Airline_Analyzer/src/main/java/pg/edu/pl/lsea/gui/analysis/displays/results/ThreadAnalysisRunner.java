package pg.edu.pl.lsea.gui.analysis.displays.results;

import pg.edu.pl.lsea.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.gui.analysis.AnalysisPanel;
import pg.edu.pl.lsea.gui.analysis.utils.DataStorageAnalysis;

import javax.swing.*;
import java.util.List;

/**
 * Class responsible for the thread analysis
 */
public class ThreadAnalysisRunner extends BaseRunner {
    private AnalysisPanel analysisPanel;

    public ThreadAnalysisRunner(AnalysisPanel analysisPanel) {
        this.analysisPanel = analysisPanel;
    }

    /**
     * Function for running the thread analysis.
     * @param parallel - true (is parallel) or false (is sequential)
     * @param threads - number of threads (will work only if parallel)
     */
    public void runAnalysis(boolean parallel, int threads) {
        List<EnrichedFlight> enrichedFlights = DataStorageAnalysis.prepareFlights();
        List<Aircraft> aircrafts = DataStorageAnalysis.prepareAircrafts();

        log("Starting analysis...");
        log(parallel ? "Parallel analysis selected." : "Sequential (single-threaded) analysis selected.");

        long start = System.currentTimeMillis();
        List<List<EnrichedFlight>> groupedModel = groupByModel(enrichedFlights, aircrafts, parallel, threads);
        List<List<EnrichedFlight>> groupedOperator = groupByOperator(enrichedFlights, aircrafts, parallel, threads);
        long end = System.currentTimeMillis();

        long duration = end - start;
        log("Grouping complete.");
        log("Total execution time: " + duration + " ms");

        analyzeGroupedData(groupedModel, "Grouped by Model");
        analyzeGroupedData(groupedOperator, "Grouped by Operator");

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
    @Override
    protected void log(String msg) {
        messageLog.append(msg).append("\n");
        if (analysisPanel != null) {
            SwingUtilities.invokeLater(() -> analysisPanel.updateAnalysisProgress(msg));
        }
    }
}
