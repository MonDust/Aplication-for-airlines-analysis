package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;

import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.List;


/**
 * This class provides analysis methods for grouping enriched flight data by operators and aircraft models,
 * and calculating metrics such as counts and average durations.
 * It gives data needed for the plot.
 */
public class GroupedAndForPlotAnalysis extends TopNAnalysis {

    /**
     * Constructor for the class.
     */
    public GroupedAndForPlotAnalysis() {
    }

    /**
     * Function to get the average time per operator using Properties calculator.
     * It will get the number of operators set by NUMBER_OF_MOST_POPULAR_OPERATORS.
     *  @return A list of Output containing the average times for each operator
     */
    public List<Output> getAverageTimesForOperators() {
        return propertiesCalculator.giveAllAverages(getGroupedFlightsByTopNOperator());
    }

    /**
     * Function to get the average time per operator using Properties calculator.
     * @param topN number of top operators to consider
     * @return A list of Output containing the average times for each operator
     */
    public List<Output> getAverageTimesForOperators(int topN) {
        return propertiesCalculator.giveAllAverages(getGroupedFlightsByTopNOperator(topN));
    }

    /**
     * Function to get the average time per model using Properties calculator.
     * It will get the number of operators set by NUMBER_OF_MOST_POPULAR_OPERATORS.
     *  @return A list of Output containing the average times for each model
     */
    public List<Output> getAverageTimesForModels() {
        return propertiesCalculator.giveAllAverages(getGroupedFlightsByTopNModel());
    }

    /**
     * Function to get the average time per model using Properties calculator.
     * @param topN number of top operators to consider
     * @return A list of Output containing the average times for each model
     */
    public List<Output> getAverageTimesForModels(int topN) {
        return propertiesCalculator.giveAllAverages(getGroupedFlightsByTopNModel(topN));
    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list.
     * The function gives all of them, and not just certain amount - not sorted.
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> giveAllAverages_groupedByModel() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(storage.getEnrichedFlights(), storage.getAircrafts(), 8);
        return propertiesCalculator.giveAllAverages(listOfLists_model);
    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list.
     * The function gives all of them, and not just certain amount - not sorted.
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> giveAllAverages_groupedByOperator() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByOperator(storage.getEnrichedFlights(), storage.getAircrafts(), 8);
        return propertiesCalculator.giveAllAverages(listOfLists_model);
    }
}
