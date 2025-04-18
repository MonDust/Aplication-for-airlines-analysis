package pg.edu.pl.lsea.backend.analysis;

import pg.edu.pl.lsea.backend.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.backend.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.*;

public class TopNAnalysis extends BaseAnalysis{
    public TopNAnalysis() {}

    public List<List<EnrichedFlight>> getTopNModels() {
        return getTopNModels(NUMBER_OF_MOST_POPULAR_MODELS);
    }

    public List<List<EnrichedFlight>> getTopNModels(int topN){
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        ParallelGroupingTool tool = new ParallelGroupingTool();
        SortingCalculator sortCalc = new SortingCalculator();

        List<List<EnrichedFlight>> listFlights = tool.groupFlightsByModel(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);

        return sortCalc.giveTopNOperators(listFlights, topN);
    }

    public List<List<EnrichedFlight>> getTopNOperators() {
        return getTopNModels(NUMBER_OF_MOST_POPULAR_OPERATORS);
    }

    public List<List<EnrichedFlight>> getTopNOperators(int topN){
        List<EnrichedFlight> flights = prepareFlights();
        List<Aircraft> aircrafts = prepareAircrafts();

        ParallelGroupingTool tool = new ParallelGroupingTool();
        SortingCalculator sortCalc = new SortingCalculator();

        List<List<EnrichedFlight>> listFlights = tool.groupFlightsByOperator(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);

        return sortCalc.giveTopNOperators(listFlights, topN);
    }

    /**
     * Method to perform the percentage of long flights for Top N operators analysis and display the results.
     * N established by constant - NUMBER_OF_MOST_POPULAR_OPERATORS.
     */
    public List<Output> getTopNOperatorsPercentages() {
        List<List<EnrichedFlight>> flights = getTopNOperators();
        PropertiesCalculator propCalc = new PropertiesCalculator();
        return propCalc.givePercentageOfLongFlights(flights);
    }

}
