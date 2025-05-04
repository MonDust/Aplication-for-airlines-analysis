package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;


import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;
import pg.edu.pl.lsea.backend.services.analysis.BaseAnalysis;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.*;

/**
 * Basic Default Analysis.
 * - Grouping functions, Top N, Percentage of Long Flights.
 * ----
 *  Full analysis: FullGroupedTopNAnalysis.
 *  Use this one if only Basic Analysis is needed, otherwise use the above.
 */
public class BasicDefaultAnalysis extends BaseAnalysis {

    /**
     * Constructor for the class.
     * @param flightRepo repository
     * @param flightToResponseMapper mapper
     * @param enrichedFlightRepo repository
     * @param enrichedFlightToResponseMapper mapper
     * @param aircraftRepo repository
     * @param aircraftToResponseMapper mapper
     */
    public BasicDefaultAnalysis(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper,
                                EnrichedFlightRepo enrichedFlightRepo, EnrichedFlightToResponseMapper enrichedFlightToResponseMapper,
                                AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper ) {

        super(flightRepo, flightToResponseMapper,
                enrichedFlightRepo, enrichedFlightToResponseMapper,
                aircraftRepo, aircraftToResponseMapper);
    }

    ///  OTHER - INT, LIST OF LISTS OF ENRICHEDFLIGHTS etc. ///

    // GET LONG FLIGHT - BOTH GROUPINGS //


    /**
     * Returns list of list which is containing any long flights, grouped by models.
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return groupingTool.findLongFlightsForEachModel(listOfLists_model);
    }

    /**
     * Returns list of list which is containing any long flights, grouped by operators.
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachOperator() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByOperator(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return groupingTool.findLongFlightsForEachModel(listOfLists_model);
    }

    /**
     * Calculates average time in air for list of flights.
     * @return int - average time per input flights
     */
    public int calculateAverageTimeInAir() {
        return propertiesCalculator.calculateAverageTimeInAir(enrichedFlightRepo.findAll());
    }


    /// LIST OF OUTPUTS ///

    // PERCENTAGE OF LONG FLIGHTS - BOTH GROUPINGS //

    /**
     * This function gives percentage of flights that classify as long per each list in list of lists.
     * The flights are grouped by models.
     * ALL MODELS
     * @return list of outputs; value - percentage of flight that classify as long stored in output format
     */
    public List<Output> getPercentageOfLongFlights_ModelGrouping() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), NUMBER_OF_DEFAULT_THREADS);
        return propertiesCalculator.givePercentageOfLongFlights(listOfLists_model);
    }

    /**
     * This function gives percentage of flights that classify as long per each list in list of lists.
     * The flights are grouped by operators.
     * ALL OPERATORS
     * @return list of outputs; value - percentage of flight that classify as long stored in output format
     */
    public List<Output> getPercentageOfLongFlights_OperatorGrouping() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByOperator(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), NUMBER_OF_DEFAULT_THREADS);
        return propertiesCalculator.givePercentageOfLongFlights(listOfLists_model);
    }

    /**
     * Sorting flights by the time of flight
     * @return list of outputs - icao24s with value - time of flight
     */
    public List<Output> getListSortedByTimeOfFlights() {
        return sortingCalculator.sortByTimeOfFlights(enrichedFlightRepo.findAll());
    }

    /**
     * Sorting flights by the number of flights
     * @return list of outputs - icao24s with value - amount of flights
     */
    public List<Output> getListSortedByNumberOfFlights() {
        return sortingCalculator.sortByAmountOfFlights(enrichedFlightRepo.findAll());
    }


    // Grouping and TopN //

    /**
     * Get grouped by the operators - result: output with certain icaos representing the operators and number of flights.
     * Returns a list of outputs for the top operators, each output containing an ICAO24 identifier
     * and the number of flights for that operator.
     * Possible to pass an argument to specify number of top operators.
     * @return List of Output representing the number of flights for the specified number of top operators. (one of the icaos and size)
     */
    public List<Output> getGroupedTopNOperators() {
        return getGroupedTopNOperators(NUMBER_OF_MOST_POPULAR_OPERATORS);
    }

    /**
     * Get grouped by the operators - result with a specified number of top operators.
     * Returns a list of outputs for the top N operators, each output containing an ICAO24 identifier
     * and the number of flights for that operator.
     * @param topN the number of top operators to consider
     * @return List of Output representing the number of flights for the specified number of top operators.
     */
    public List<Output> getGroupedTopNOperators(int topN) {
        List<List<EnrichedFlight>> enrichedList = getGroupedFlightsByTopNOperator(topN);
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
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * Possible to pass an argument to specify number of top models.
     * @return List of Output representing the number of flights for the specified number of top models. (one of the icaos and size)
     */
    public List<Output> getGroupedTopNModels() {
        return getGroupedTopNModels(NUMBER_OF_MOST_POPULAR_MODELS);
    }

    /**
     * Get grouped by the models - result with a specified number of top models.
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * @param topN the number of top models to consider
     * @return List of Output representing the number of flights for the specified number of top models.
     */
    public List<Output> getGroupedTopNModels(int topN) {
        List<List<EnrichedFlight>> enrichedList = getGroupedFlightsByTopNModel(topN);
        List<Output> outputList = new ArrayList<>();

        // get one icao representing models and value representing number of flights
        for (List<EnrichedFlight> enrichedFlights : enrichedList) {
            Output o = new Output(enrichedFlights.getFirst().getIcao24(), enrichedFlights.size());
            outputList.add(o);
        }
        return outputList;
    }

    ///  LIST OF LISTS OF ENRICHED FLIGHTS ///


    /**
     * Get enriched flights grouped by the operator, defaulting to top 5 operators.
     * Possible to pass an argument to specify number of top operators.
     * @return List of lists of enriched flights, each representing a top operator.
     */
    protected List<List<EnrichedFlight>> getGroupedFlightsByTopNOperator() {
        return getGroupedFlightsByTopNOperator(NUMBER_OF_MOST_POPULAR_OPERATORS);
    }

    /**
     * Get enriched flights grouped by the operator, then processed for Top N Operators.
     * @param topN The number of top operators to consider.
     * @return List of lists of enriched flights, each representing a top operator.
     */
    protected List<List<EnrichedFlight>> getGroupedFlightsByTopNOperator(int topN) {
        // Grouping flights by operator (in parallel)
        List<List<EnrichedFlight>> groupedFlights = parallelGroupingTool.groupFlightsByOperator(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), NUMBER_OF_DEFAULT_THREADS);

        // Sorting and getting top N operators
        return sortingCalculator.giveTopN_byAttribute(groupedFlights, topN);
    }

    /**
     * Get enriched flights grouped by the model, defaulting to top 5 models.
     * Possible to pass an argument to specify number of top models.
     * @return List of lists of enriched flights, each representing a top model.
     */
    protected List<List<EnrichedFlight>> getGroupedFlightsByTopNModel() {
        return getGroupedFlightsByTopNModel(NUMBER_OF_MOST_POPULAR_MODELS);
    }

    /**
     * Get enriched flights grouped by the model.
     * @param topN The number of top models to consider.
     * @return List of lists of enriched flights, each representing a top model.
     */
    protected List<List<EnrichedFlight>> getGroupedFlightsByTopNModel(int topN) {
        // Grouping flights by model (in parallel)
        List<List<EnrichedFlight>> groupedFlights = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), NUMBER_OF_DEFAULT_THREADS);

        // Sorting and getting top N models
        return sortingCalculator.giveTopN_byAttribute(groupedFlights, topN);
    }

}
