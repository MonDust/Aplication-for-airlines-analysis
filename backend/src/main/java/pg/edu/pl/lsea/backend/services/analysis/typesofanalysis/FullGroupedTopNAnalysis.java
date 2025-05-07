package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;

import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;
import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;

import java.util.List;


/**
 * This class provides analysis methods for grouping enriched flight data by operators and aircraft models,
 * and calculating metrics such as counts and average durations.
 * It gives data needed for the plot.
 * -----
 *  Class for analyzing flight data, it is used to determine the most popular aircraft models and operators.
 *  Additionally, it provides functionality to find percentages of long flights for top n operators.
 */
public class FullGroupedTopNAnalysis extends BasicDefaultAnalysis {

    /**
     * Constructor for the class.
     * @param flightRepo - repository; h2 database
     * @param flightToResponseMapper - mapper; h2 database
     * @param enrichedFlightRepo - repository; h2 database
     * @param enrichedFlightToResponseMapper - mapper; h2 database
     * @param aircraftRepo - repository; h2 database
     * @param aircraftToResponseMapper - mapper; h2 database
     */
    public FullGroupedTopNAnalysis(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper,
                                   EnrichedFlightRepo enrichedFlightRepo, EnrichedFlightToResponseMapper enrichedFlightToResponseMapper,
                                   AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper ) {

        super(flightRepo, flightToResponseMapper,
                enrichedFlightRepo, enrichedFlightToResponseMapper,
                aircraftRepo, aircraftToResponseMapper);
    }


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
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return propertiesCalculator.giveAllAverages(listOfLists_model);
    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list.
     * The function gives all of them, and not just certain amount - not sorted.
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> giveAllAverages_groupedByOperator() {
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByOperator(enrichedFlightRepo.findAll(), aircraftRepo.findAll(), 8);
        return propertiesCalculator.giveAllAverages(listOfLists_model);
    }
}
