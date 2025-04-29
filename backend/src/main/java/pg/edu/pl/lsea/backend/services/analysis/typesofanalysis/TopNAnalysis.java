package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;

import pg.edu.pl.lsea.backend.services.analysis.BaseAnalysis;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.*;

/**
 * Class for analyzing flight data, it is used to determine the most popular aircraft models and operators.
 * Additionally, it provides functionality to find percentages of long flights for top n operators.
 * --
 * Full analysis: GroupedAndForPlotAnalysis.
 * Use this one if only TopNAnalysis is needed, otherwise use the above.
 */
public class TopNAnalysis extends BaseAnalysis {
    /**
     * Default constructor for TopNAnalysis.
     */
    public TopNAnalysis() {}

    /**
     * Method to perform the percentage of long flights for top n operators analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_OPERATORS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNOperatorsPercentages() {
        List<List<EnrichedFlight>> flights = getGroupedFlightsByTopNOperator();
        return propertiesCalculator.givePercentageOfLongFlights(flights);
    }

    /**
     * Method to perform the percentage of long flights for top n operators analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_OPERATORS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNOperatorsPercentages(int topN) {
        List<List<EnrichedFlight>> flights = getGroupedFlightsByTopNOperator(topN);
        return propertiesCalculator.givePercentageOfLongFlights(flights);
    }

    /**
     * Method to perform the percentage of long flights for top n models analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_MODELS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNModelsPercentages() {
        List<List<EnrichedFlight>> flights = getGroupedFlightsByTopNModel();
        return propertiesCalculator.givePercentageOfLongFlights(flights);
    }

    /**
     * Method to perform the percentage of long flights for top n models analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_MODELS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNModelsPercentages(int topN) {
        List<List<EnrichedFlight>> flights = getGroupedFlightsByTopNModel(topN);
        return propertiesCalculator.givePercentageOfLongFlights(flights);
    }

}
