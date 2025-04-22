package pg.edu.pl.lsea.backend.analysis.typesofanalysis;

import pg.edu.pl.lsea.backend.analysis.BaseAnalysis;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.*;
/**
 * This class provides analysis methods for grouping enriched flight data by operators and aircraft models,
 * and calculating metrics such as counts and average durations.
 * It gives data needed for the plot.
 */
public class GroupedAndForPlotAnalysis extends BaseAnalysis {

    /**
     * Constructor for the class.
     */
    public GroupedAndForPlotAnalysis() {
    }

    /**
     * Get grouped by the operators - result: output with certain icaos representing the operators and number of flights.
     * @return list of outputs with number of flights for certain operators (one of the icaos and size)
     */
    public List<Output> getGroupedOperators() {
        List<List<EnrichedFlight>> enrichedList = getGroupedEnrichedFlightsByOperator();
        List<Output> outputList = new ArrayList<>();

        // get one icao representing operator and value representing number of flights
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    /**
     * Get grouped by the operators - result with a specified number of top operators.
     * @param topN the number of top operators to consider
     * @return list of outputs with number of flights for certain operators
     */
    public List<Output> getGroupedOperators(int topN) {
        List<List<EnrichedFlight>> enrichedList = getGroupedEnrichedFlightsByOperator(topN);
        List<Output> outputList = new ArrayList<>();

        // get one icao representing operator and value representing number of flights
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    /**
     * Get grouped by the models - result: output with certain icaos representing the models and number of flights.
     * @return list of outputs with number of flights for ceraint models (one of the icaos and size)
     */
    public List<Output> getGroupedModels() {
        List<List<EnrichedFlight>> enrichedList = getGroupedEnrichedFlightsByModel();
        List<Output> outputList = new ArrayList<>();

        // get one icao representing models and value representing number of flights
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    /**
     * Get grouped by the models - result with a specified number of top models.
     * @param topN the number of top models to consider
     * @return list of outputs with number of flights for certain models
     */
    public List<Output> getGroupedModels(int topN) {
        List<List<EnrichedFlight>> enrichedList = getGroupedEnrichedFlightsByModel(topN);
        List<Output> outputList = new ArrayList<>();

        // get one icao representing models and value representing number of flights
        for (int i = 0; i < enrichedList.size(); i++) {
            Output o = new Output(enrichedList.get(i).getFirst().getIcao24(),enrichedList.get(i).size());
            outputList.add(o);
        }
        return outputList;
    }

    /**
     * Get enriched flights grouped by the operator, defaulting to top 5 operators.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlightsByOperator() {
        return getGroupedEnrichedFlightsByOperator(NUMBER_OF_MOST_POPULAR_OPERATORS);
    }

    /**
     * Get enriched flights grouped by the operator, then processed for Top N Operators.
     * @param topN The number of top operators to consider.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlightsByOperator(int topN) {
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        // Grouping flights by operator (in parallel)
        List<List<EnrichedFlight>> groupedFlights = parallelGroupingTool.groupFlightsByOperator(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);

        // Sorting and getting top N operators
        return sortingCalculator.giveTopNOperators(groupedFlights, topN);
    }

    /**
     * Get enriched flights grouped by the model, defaulting to top 5 models.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlightsByModel() {
        return getGroupedEnrichedFlightsByModel(NUMBER_OF_MOST_POPULAR_MODELS);
    }

    /**
     * Get enriched flights grouped by the model.
     * @param topN The number of top models to consider.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlightsByModel(int topN) {
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        // Grouping flights by model (in parallel)
        // Grouping flights by operator (in parallel)
        List<List<EnrichedFlight>> groupedFlights = parallelGroupingTool.groupFlightsByModel(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);

        // Sorting and getting top N operators
        return sortingCalculator.giveTopNOperators(groupedFlights, topN);
    }

    /**
     * Function to get the average time per operator using Properties calculator.
     * It will get the number of operators set by NUMBER_OF_MOST_POPULAR_OPERATORS.
     *  @return A list of Output containing the average times for each operator
     */
    public List<Output> getAverageTimesForOperators() {
        return propertiesCalculator.printAllAverages(getGroupedEnrichedFlightsByOperator());
    }

    /**
     * Function to get the average time per operator using Properties calculator.
     * @param topN number of top operators to consider
     * @return A list of Output containing the average times for each operator
     */
    public List<Output> getAverageTimesForOperators(int topN) {
        return propertiesCalculator.printAllAverages(getGroupedEnrichedFlightsByOperator(topN));
    }
}
