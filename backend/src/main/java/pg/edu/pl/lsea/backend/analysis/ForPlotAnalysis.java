package pg.edu.pl.lsea.backend.analysis;

import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.*;

public class ForPlotAnalysis extends BaseAnalysis {

    public ForPlotAnalysis() {}

    /**
     * Get enriched flights grouped by the operator, defaulting to top 5 operators.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlightsByOperator() {
        return getGroupedEnrichedFlightsByOperator(NUMBER_OF_MOST_POPULAR_OPERATORS);
    }

    /**
     * Get enriched flights grouped by the operator, then processed for Top N Operators.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlightsByOperator(int topN) {
        List<List<EnrichedFlight>> listOfLists;
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        SortingCalculator sortCalc = new SortingCalculator();
        ParallelGroupingTool tool = new ParallelGroupingTool();

        List<List<EnrichedFlight>> listFlights = tool.groupFlightsByOperator(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);
        listOfLists = sortCalc.giveTopNOperators(listFlights, topN);

        return listOfLists;
    }

    /**
     * Function to get the average time per operator using Properties calculator.
     * It will get the number of operators set by NUMBER_OF_MOST_POPULAR_OPERATORS.
     */
    public List<Output> getAverageTimesForOperators() {
        // Get the list of averages for the operators
        PropertiesCalculator propCalc = new PropertiesCalculator();

        return propCalc.printAllAverages(getGroupedEnrichedFlightsByOperator());
    }

    /**
     * Get operator from output entity.
     * @param output - output
     * @return String with the Operator
     */
    public String getOperator(Output output) {
        AircraftParser parser = new AircraftParser(storage.getAircrafts());
        return parser.getAircraftByIcao(output.getIcao24()).getOperator();
    }



}
