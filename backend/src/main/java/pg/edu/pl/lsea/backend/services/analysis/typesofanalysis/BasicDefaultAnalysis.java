package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;


import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.entities.*;
import pg.edu.pl.lsea.backend.repositories.*;
import pg.edu.pl.lsea.backend.services.analysis.BaseAnalysis;

import java.util.Comparator;
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
    public List<Output> getGroupedTopNOperators(OperatorRepo operatorRepo) {
        return getGroupedTopNOperators(operatorRepo, NUMBER_OF_MOST_POPULAR_OPERATORS);
    }



    /**
     * Get grouped by the operators - result with a specified number of top operators.
     * Returns a list of outputs for the top N operators, each output containing an ICAO24 identifier
     * and the number of flights for that operator.
     * @param operatorRepo operators stored in the database
     * @param topN the number of top operators to consider
     * @return List of Output representing the number of flights for the specified number of top operators.
     */
    public List<Output> getGroupedTopNOperators(OperatorRepo operatorRepo, int topN) {
        List<Operator> operators = operatorRepo.findAll();

        // Compute total flights per operator
        List<Output> outputList = operators.stream()
                .map(operator -> {
                    int totalFlights = operator.getAircrafts().stream()
                            .mapToInt(aircraft -> aircraft.getFlights().size()) // assumes Aircraft has getFlights()
                            .sum();

                    String icao24 = operator.getAircrafts().stream()
                            .findFirst()
                            .map(Aircraft::getIcao24)
                            .orElse("UNKNOWN");

                    return new Output(icao24, totalFlights);
                })
                .sorted(Comparator.comparingInt(Output::getValue).reversed()) // sort by flights descending
                .limit(topN)
                .toList();

        return outputList;
    }

    /**
     * Get grouped by the models - result: output with certain icaos representing the models and number of flights.
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * Possible to pass an argument to specify number of top models.
     * @return List of Output representing the number of flights for the specified number of top models. (one of the icaos and size)
     */
    public List<Output> getGroupedTopNModels(ModelRepo modelRepo) {
        return getGroupedTopNModels(modelRepo, NUMBER_OF_MOST_POPULAR_MODELS);
    }

    /**
     * Get grouped by the models - result with a specified number of top models.
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * @param topN the number of top models to consider
     * @return List of Output representing the number of flights for the specified number of top models.
     */
    public List<Output> getGroupedTopNModels(ModelRepo modelRepo, int topN) {
        List<Model> models = modelRepo.findAll();

        List<Output> outputList = models.stream()
                .map(model -> {
                    int totalFlights = model.getAircrafts().stream()
                            .mapToInt(ac -> ac.getFlights().size())
                            .sum();

                    String icao24 = model.getAircrafts().stream()
                            .findFirst()
                            .map(Aircraft::getIcao24)
                            .orElse("UNKNOWN");

                    return new Output(icao24, totalFlights);
                })
                .sorted(Comparator.comparingInt(Output::getValue).reversed())
                .limit(topN)
                .toList();

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
