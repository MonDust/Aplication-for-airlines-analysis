package pg.edu.pl.lsea.gui.analysis.displays.results;

import pg.edu.pl.lsea.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.analysis.utils.AircraftParser;
import pg.edu.pl.lsea.gui.analysis.utils.DataStorageAnalysis;

import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.NUMBER_OF_MOST_POPULAR_OPERATORS;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_DEFAULT_THREADS;

/**
 * Class responsible for returning Top N Operators - showing a windows with an appropriate message.
 */
public class TopNOperatorsPercentage extends BaseRunner {

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

        List<Output> percantages = new PropertiesCalculator().givePercentageOfLongFlights(topOperators);

        // Log the results
        log("Top " + n + " Operators:");
        for (int i = 0; i < topOperators.size(); i++) {
            System.out.println(topOperators.get(i));
            log("Operator " + parser.getAircraftByIcao(percantages.get(i).getIcao24()).getOperator() + ": " + percantages.get(i).Value + " %");
            for (EnrichedFlight flight : topOperators.get(i)) {
                System.out.println();
                System.out.println(flight + " | " + parser.getAircraftByIcao(flight.getIcao24()));
            }
        }
    }
}
