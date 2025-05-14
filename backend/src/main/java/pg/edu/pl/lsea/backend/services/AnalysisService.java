package pg.edu.pl.lsea.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import pg.edu.pl.lsea.backend.controllers.dto.mapper.AircraftToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.EnrichedFlightToResponseMapper;
import pg.edu.pl.lsea.backend.controllers.dto.mapper.FlightToResponseMapper;

import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.grouping.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.entities.analysis.Output;

import pg.edu.pl.lsea.backend.repositories.*;

import pg.edu.pl.lsea.backend.repositories.original.AircraftRepo;
import pg.edu.pl.lsea.backend.repositories.original.FlightRepo;
import pg.edu.pl.lsea.backend.services.analysis.typesofanalysis.FullGroupedTopNAnalysis;

import java.util.List;

/**
 * Service that handles logic behind requests from controller AnalysisController
 */
@Service
@Transactional
public class AnalysisService {

    // Analysis
    private final FullGroupedTopNAnalysis analysisFunc;

    private final OperatorRepo operatorRepo;
    private final ModelRepo modelRepo;


    /**
     * Constructor for AnalysisService class
    */
    public AnalysisService(FullGroupedTopNAnalysis analysisFunc, OperatorRepo operatorRepo, ModelRepo modelRepo) {
        this.analysisFunc = analysisFunc;
        this.operatorRepo = operatorRepo;
        this.modelRepo = modelRepo;
    }

    // NUMBER OF FLIGHTS CONNECTED TO THE OPERATOR - TOP N OPERATOR GROUPING //

    /**
     * Get grouped by the operators - result with a specified number of top operators.
     * Returns a list of outputs for the top N operators, each output containing an ICAO24 identifier
     * and the number of flights for that operator.
     * @param topN the number of top operators to consider
     * @return List of Output representing the number of flights for the specified number of top operators.
     */
    public List<Output> getTopNOperatorWithNumberOfFlights(int topN) {
        return analysisFunc.getGroupedTopNOperators(operatorRepo, topN);
    }

    // NUMBER OF FLIGHTS CONNECTED TO THE MODEL - TOP N MODEL GROUPING //

    /**
     * Get grouped by the models - result with a specified number of top models.
     * Returns a list of outputs for the top N aircraft models, each output containing an ICAO24 identifier
     * and the number of flights for that model.
     * @param topN the number of top models to consider
     * @return List of Output representing the number of flights for the specified number of top models.
     */
    public List<Output> getTopNModelWithNumberOfFlights(int topN) {
        return analysisFunc.getGroupedTopNModels(modelRepo, topN);
    }

    // PERCENTAGE OF LONG FLIGHTS - TOP N OPERATORS GROUPING //

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
    public List<Output> getTopNAverageTime_GroupedByOperator(int topN) {
        return analysisFunc.getAverageTimesForOperators(topN);
    }
}
