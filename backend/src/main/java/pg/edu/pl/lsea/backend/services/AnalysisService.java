package pg.edu.pl.lsea.backend.services;

import org.springframework.stereotype.Service;

import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;

import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import pg.edu.pl.lsea.backend.repositories.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.FlightRepo;

import pg.edu.pl.lsea.backend.services.analysis.typesofanalysis.FullGroupedTopNAnalysis;

import java.util.List;

/**
 * Service that handles logic behind requests from controller AnalysisController
 */
@Service
public class AnalysisService {

    // Analysis
    private final FullGroupedTopNAnalysis analysisFunc;


    /**
     * Constructor for AnalysisService class
     * @param flightRepo - repository; h2 database
     * @param flightToResponseMapper - mapper; h2 database
     * @param enrichedFlightRepo - repository; h2 database
     * @param enrichedFlightToResponseMapper - mapper; h2 database
     * @param aircraftRepo - repository; h2 database
     * @param aircraftToResponseMapper - mapper; h2 database
     */
    public AnalysisService(FlightRepo flightRepo, FlightToResponseMapper flightToResponseMapper,
                           EnrichedFlightRepo enrichedFlightRepo, EnrichedFlightToResponseMapper enrichedFlightToResponseMapper,
                           AircraftRepo aircraftRepo, AircraftToResponseMapper aircraftToResponseMapper ) {
        this.analysisFunc = new FullGroupedTopNAnalysis(flightRepo, flightToResponseMapper,
                enrichedFlightRepo, enrichedFlightToResponseMapper,
                aircraftRepo, aircraftToResponseMapper);
    }


    // SORTING //


    /**
     * Gives amount of flights per each ICAO
     * @return list with amount of flights per icao24 written in output objects
     */
    public List<Output> sortByAmountOfFlights() {
        return analysisFunc.getListSortedByNumberOfFlights();
    }

    /**
     * Gives amout of time of flights per each ICAO
     * @return list with amount of time in air per icao24 written in output objects
     */
    public List<Output> sortByTimeOfFlights() {
        return analysisFunc.getListSortedByNumberOfFlights();
    }


    // GIVE PERCENTAGE OF LONG FLIGHTS - BOTH GROUPINGS //


    /**
     * This function gives percentage of flights that classify as long per each list in list of lists.
     * The flights are grouped by models.
     * @return list of outputs; value - percentage of flight that classify as long stored in output format
     */
    public List<Output> givePercentageOfLongFlights_ModelGrouping() {
        return analysisFunc.getPercentageOfLongFlights_ModelGrouping();
    }

    /**
     * This function gives percentage of flights that classify as long per each list in list of lists.
     * The flights are grouped by operators.
     * @return list of outputs; value - percentage of flight that classify as long stored in output format
     */
    public List<Output> givePercentageOfLongFlights_OperatorGrouping() {
        return analysisFunc.getPercentageOfLongFlights_OperatorGrouping();
    }


    // GIVE ALL AVERAGES - BOTH GROUPINGS //

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> giveAllAverages_groupedByModel() {
        return analysisFunc.giveAllAverages_groupedByModel();
    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> giveAllAverages_groupedByOperator() {
        return analysisFunc.giveAllAverages_groupedByOperator();
    }

    ///  OTHER FUNCTIONS ///

    // AVERAGE TIME IN AIR - GROUPED BY OPERATORS //

    /**
     * Calculates average time in air for list of flights.
     * @return averred time per inputed flights.
     */
    public int calculateAverageTimeInAir() {
        return analysisFunc.calculateAverageTimeInAir();
    }

    // FIND LONG FLIGHTS - GROUPED BY MODELS //

    /**
     * Returns list of list which is containing any long flights
     * @return list of list which is containing any long flights
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {
        return analysisFunc.findLongFlightsForEachModel();
    }

    ///  NEEDED FOR FRONTEND -> CONTROLLER IS CONNECTED ///

    // NUMBER OF FLIGHTS CONNECTED TO THE OPERATOR - TOP N OPERATOR GROUPING //

    /**
     * Get grouped by the operators - result: output with certain icaos representing the operators and number of flights.
     * Returns a list of outputs for the top operators, each output containing an ICAO24 identifier
     * and the number of flights for that operator.
     * Possible to pass an argument to specify number of top operators.
     * @return List of Output representing the number of flights for the specified number of top operators. (one of the icaos and size)
     */
    public List<Output> getTopNOperatorWithNumberOfFlights() {
        return analysisFunc.getGroupedTopNOperators();
    }

    /**
     * Get grouped by the operators - result with a specified number of top operators.
     * Returns a list of outputs for the top N operators, each output containing an ICAO24 identifier
     * and the number of flights for that operator.
     * @param topN the number of top operators to consider
     * @return List of Output representing the number of flights for the specified number of top operators.
     */
    public List<Output> getTopNOperatorWithNumberOfFlights(int topN) {
        return analysisFunc.getGroupedTopNOperators(topN);
    }

    // NUMBER OF FLIGHTS CONNECTED TO THE MODEL - TOP N MODEL GROUPING //

    /**
     * Get grouped by the models - result: output with certain icaos representing the models and number of flights.
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * Possible to pass an argument to specify number of top models.
     * @return List of Output representing the number of flights for the specified number of top models. (one of the icaos and size)
     */
    public List<Output> getTopNModelWithNumberOfFlights() {
        return analysisFunc.getGroupedTopNModels();
    }

    /**
     * Get grouped by the models - result with a specified number of top models.
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * @param topN the number of top models to consider
     * @return List of Output representing the number of flights for the specified number of top models.
     */
    public List<Output> getTopNModelWithNumberOfFlights(int topN) {
        return analysisFunc.getGroupedTopNModels(topN);
    }

    // PERCENTAGE OF LONG FLIGHTS - TOP N OPERATORS GROUPING //

    /**
     * Method to perform the percentage of long flights for top n operators analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_OPERATORS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNPercentageOfLongFlights_GroupedByOperator() {
        return analysisFunc.getTopNOperatorsPercentages();
    }

    /**
     * Method to perform the percentage of long flights for top n operators analysis and display the results.
     * The value of N is defined by the NUMBER_OF_MOST_POPULAR_OPERATORS constant.
     * @return A list of Output objects containing percentage of long flights per operator.
     */
    public List<Output> getTopNPercentageOfLongFlights_GroupedByOperator(int topN) {
        return analysisFunc.getTopNOperatorsPercentages(topN);
    }

    // AVERAGE TIME IN THE AIR - TOP N OPERATORS GROUPING //

    /**
     * Function to get the average time per operator using Properties calculator.
     * It will get the number of operators set by NUMBER_OF_MOST_POPULAR_OPERATORS.
     *  @return A list of Output containing the average times for each operator
     */
    public List<Output> getTopNAverageTime_GroupedByOperator() {
        return analysisFunc.getAverageTimesForOperators();
    }

    /**
     * Function to get the average time per operator using Properties calculator.
     * It will get the number of operators set by NUMBER_OF_MOST_POPULAR_OPERATORS.
     *  @return A list of Output containing the average times for each operator
     */
    public List<Output> getTopNAverageTime_GroupedByOperator(int topN) {
        return analysisFunc.getAverageTimesForOperators(topN);
    }

}
