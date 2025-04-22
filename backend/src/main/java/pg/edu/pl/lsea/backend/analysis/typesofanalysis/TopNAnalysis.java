package pg.edu.pl.lsea.backend.analysis.typesofanalysis;

import pg.edu.pl.lsea.backend.analysis.BaseAnalysis;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.*;

/**
 * Class for analyzing flight data, it is used to determine the most popular aircraft models and operators.
 * Additionally, it provides functionality to find percentages of long flights for top n operators.
 */
public class TopNAnalysis extends BaseAnalysis {
    /**
     * Default constructor for TopNAnalysis.
     */
    public TopNAnalysis() {}

    /**
     * Get grouped by the operators - result.
     * @return list of outputs with number of flights for certain operators (one of the icaos and size)
     */
    public List<Output> getTopNOperators_OutputList() {
        List<List<EnrichedFlight>> enrichedList = getTopNOperators();
        List<Output> outputList = new ArrayList<>();
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    public List<Output> getTopNOperators_OutputList(int topN) {
        List<List<EnrichedFlight>> enrichedList = getTopNOperators(topN);
        List<Output> outputList = new ArrayList<>();
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    /**
     *
     * @return list of outputs with number of flights for ceraint models (one of the icaos and size)
     */
    public List<Output> getGroupedModels_OutputList() {
        List<List<EnrichedFlight>> enrichedList = getTopNModels();
        List<Output> outputList = new ArrayList<>();
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    public List<Output> getTopNModels_OutputList(int topN) {
        List<List<EnrichedFlight>> enrichedList = getTopNModels(topN);
        List<Output> outputList = new ArrayList<>();
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    /**
     * Retrieves the top n most popular aircraft models based on flight data.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_MODELS constant.
     * @return A list of flight groups, each representing a top aircraft model.
     */
    public List<List<EnrichedFlight>> getTopNModels() {
        return getTopNModels(NUMBER_OF_MOST_POPULAR_MODELS);
    }

    /**
     * Retrieves the top n most popular aircraft models based on flight data.
     * @param topN The number of top aircraft models to retrieve.
     * @return A list of flight groups, each representing a top aircraft model.
     */
    public List<List<EnrichedFlight>> getTopNModels(int topN){
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        List<List<EnrichedFlight>> listFlights = parallelGroupingTool.groupFlightsByModel(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);

        return sortingCalculator.giveTopNOperators(listFlights, topN);
    }

    /**
     * Retrieves the top N most popular aircraft operators based on flight data.
     * The value of n is defined by the NUMBER_OF_MOST_POPULAR_OPERATORS constant.
     * @return A list of flight groups, each representing a top operator.
     */
    public List<List<EnrichedFlight>> getTopNOperators() {
        return getTopNModels(NUMBER_OF_MOST_POPULAR_OPERATORS);
    }

    /**
     * Retrieves the top n most popular aircraft operators based on flight data.
     * @param topN The number of top operators to retrieve.
     * @return A list of flight groups, each representing a top operator.
     */
    public List<List<EnrichedFlight>> getTopNOperators(int topN){
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        List<List<EnrichedFlight>> listFlights = parallelGroupingTool.groupFlightsByOperator(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);

        return sortingCalculator.giveTopNOperators(listFlights, topN);
    }

    /**
     * Method to perform the percentage of long flights for top n operators analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_OPERATORS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNOperatorsPercentages() {
        List<List<EnrichedFlight>> flights = getTopNOperators();
        return propertiesCalculator.givePercentageOfLongFlights(flights);
    }

}
