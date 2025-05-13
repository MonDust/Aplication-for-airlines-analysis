package pg.edu.pl.lsea.backend.services.analysis.typesofanalysis;

import org.springframework.stereotype.Component;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Model;
import pg.edu.pl.lsea.backend.entities.Operator;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.repositories.ModelRepo;
import pg.edu.pl.lsea.backend.repositories.OperatorRepo;
import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.EnrichedFlightRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.services.analysis.BaseAnalysis;

import java.util.Comparator;
import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.NUMBER_OF_DEFAULT_THREADS;


/**
 * This class provides analysis methods for grouping enriched flight data by operators and aircraft models,
 * and calculating metrics such as counts and average durations.
 * It gives data needed for the plot.
 * -----
 *  Class for analyzing flight data, it is used to determine the most popular aircraft models and operators.
 *  Additionally, it provides functionality to find percentages of long flights for top n operators.
 */
@Component
public class FullGroupedTopNAnalysis extends BaseAnalysis {

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
    public List<Output> getTopNOperatorsPercentages(int topN) {
        List<List<EnrichedFlight>> flights = getGroupedFlightsByTopNOperator(topN);
        return propertiesCalculator.givePercentageOfLongFlights(flights);
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
        // assumes Aircraft has getFlights()
        // sort by flights descending

        return operators.stream()
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

        return models.stream()
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


}
