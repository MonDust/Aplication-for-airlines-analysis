package pg.edu.pl.lsea.gui.analysis.displays.results;

import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.gui.analysis.utils.AircraftParser;
import pg.edu.pl.lsea.gui.analysis.utils.DataStorageAnalysis;

import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.NUMBER_OF_MOST_POPULAR_OPERATORS;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_DEFAULT_THREADS;

/**
 * Class responsible for returning Top N Operators - showing a windows with an appropriate message.
 */
public class TopNOperators extends BaseRunner {

    /**
     * Method to perform the Top N operators analysis and display the results.
     * n established by constant.
     */
    public void analyzeTopOperators(SortingCalculator calc, AircraftParser parser) {
        List<EnrichedFlight> flights = DataStorageAnalysis.prepareFlights();
        List<Aircraft> aircrafts = DataStorageAnalysis.prepareAircrafts();

        ParallelGroupingTool tool = new ParallelGroupingTool();
        List<List<EnrichedFlight>> listFlights = tool.groupFlightsByOperator(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);


        int n = NUMBER_OF_MOST_POPULAR_OPERATORS;
        List<List<EnrichedFlight>> topOperators = calc.giveTopNOperators(listFlights, n);

        // Log the results
        log("Top " + n + " Operators:");
        for (int i = 0; i < topOperators.size(); i++) {
            System.out.println(topOperators.get(i));
            log("Operator " + parser.getAircraftByIcao(topOperators.get(i).get(0).getIcao24()).getOperator() + ": " + topOperators.get(i).size() + " flights");
            for (EnrichedFlight flight : topOperators.get(i)) {
                System.out.println();
                System.out.println(flight + " | " + parser.getAircraftByIcao(flight.getIcao24()));
            }
        }
    }
}
