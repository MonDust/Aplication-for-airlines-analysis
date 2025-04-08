package pg.edu.pl.lsea.gui.analysis.displays.datadisplays;

import pg.edu.pl.lsea.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;

import java.util.List;

public class ThreadAnalysisRunner {

    private final StringBuilder messageLog = new StringBuilder();

    public void runAnalysis(boolean parallel, int threads, List<EnrichedFlight> enrichedFlights, List<Aircraft> aircrafts) {
        log("Starting analysis...");

        long start = System.currentTimeMillis();
        List<List<EnrichedFlight>> groupedModel;
        List<List<EnrichedFlight>> groupedOperator;

        if (parallel) {
            log("Running parallel grouping with " + threads + " threads...");
            ParallelGroupingTool tool = new ParallelGroupingTool();
            groupedModel = tool.groupFlightsByModel(enrichedFlights, aircrafts, threads);
            groupedOperator = tool.groupFlightsByOperator(enrichedFlights, aircrafts, threads);
        } else {
            log("Running single-threaded grouping...");
            GroupingTool tool = new GroupingTool();
            groupedModel = tool.groupFlightsByModel(enrichedFlights, aircrafts);
            groupedOperator = tool.groupFlightsByOperator(enrichedFlights, aircrafts);
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        log("Analysis complete.");
        log("Execution time: " + duration + " ms");

        PropertiesCalculator propertiesCalculator = new PropertiesCalculator();
        propertiesCalculator.printAllAverages(groupedModel);
        propertiesCalculator.printAllAverages(groupedOperator);
    }

    private void log(String msg) {
        messageLog.append(msg).append("\n");
    }

    public String getMessages() {
        return messageLog.toString();
    }
}
