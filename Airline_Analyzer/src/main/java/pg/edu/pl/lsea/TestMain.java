package pg.edu.pl.lsea;

/** Main method for testing **/
import pg.edu.pl.lsea.data.analyzer.GroupingTool;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.data.storage.DataStorage;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataLoader;

import java.io.File;
import java.util.List;

/**
 * Main method for testing
 **/
public class TestMain {
    public static void main(String[] args) {

        long pre_analysis_start = System.currentTimeMillis();

        // Initialize - to uncomment when whole project will be together
        // CsvDataLoader dataLoader = new CsvDataLoader();
        // DataAnalyzer dataAnalyzer = new DataAnalyzer();
        // DataCleaner dataCleaner = new DataCleaner();
        // FeatureBuilder featureBuilder = new FeatureBuilder();
        CsvDataLoader dataLoader = new CsvDataLoader();
        DataEnrichment dataEnrichment = new DataEnrichment();
        NullRemover nullRemover = new NullRemover();

        SortingCalculator correlationCalculator = new SortingCalculator();


        File fileAircrafts1 = new File("resources/aircraft-database-complete-2022-09.csv");
        File fileFlights1 = new File("resources/flight_sample_2022-09-26.csv");
        File fileFlights2 = new File("resources/flight_sample_2022-09-02.csv");
        File fileFlights3 = new File("resources/flight_sample_2022-09-03.csv");
        File fileFlights4 = new File("resources/flight_sample_2022-09-04.csv");
        File fileFlights5 = new File("resources/flight_sample_2022-09-01.csv");


        long start = System.currentTimeMillis();
        dataLoader.loadFlightsToStorage(fileFlights1);
        dataLoader.loadFlightsToStorage(fileFlights2);
        dataLoader.loadFlightsToStorage(fileFlights3);
        dataLoader.loadFlightsToStorage(fileFlights4);
        dataLoader.loadFlightsToStorage(fileFlights5);
        dataLoader.loadFlightsToStorage(fileFlights1);
        dataLoader.loadFlightsToStorage(fileFlights2);
        long end = System.currentTimeMillis();
        System.out.println(end-start);


        start = System.currentTimeMillis();
        dataLoader.loadAircraftsToStorage(fileAircrafts1);
        dataLoader.loadAircraftsToStorage(fileAircrafts1);
        end = System.currentTimeMillis();
        System.out.println(end-start);

        System.out.println(DataStorage.getInstance().countAircrafts());
        System.out.println(DataStorage.getInstance().countFlights());
        // Feature engineering (adding new features/properties to Flights)

        List<Aircraft> aircrafts = DataStorage.getInstance().getAircrafts();

        List<Flight> flights = DataStorage.getInstance().getFlights();
        System.out.println(flights.stream().findAny());
        long pre_analysis_end = System.currentTimeMillis();

        // --------------------------------------- SINGLE-THREADED ANALYSIS ---------------------------------------
        long single_analysis_start = System.currentTimeMillis();

        // Clean the data
        nullRemover.TransformAircrafts(aircrafts);
        nullRemover.TransformFlights(flights);
        List<EnrichedFlight> enrichedFlights;
        enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flights);

        // Sort the data
        correlationCalculator.analyzeDataForDashbord(aircrafts, enrichedFlights);

        // Firstly, group aircraft by models and store a list of flights for each model
        GroupingTool groupingTool = new GroupingTool();
        List<List<EnrichedFlight>> listOfLists_model = groupingTool.groupFlightsByModel(enrichedFlights, aircrafts);
        List<List<EnrichedFlight>> listOfLists_operator = groupingTool.groupFlightsByOperator(enrichedFlights, aircrafts);
        List<List<EnrichedFlight>> longFlights = groupingTool.findLongFlightsForEachModel(listOfLists_model);

        // Secondly, for each model group, print average time in the air of all flights
        PropertiesCalculator propertiesTool = new PropertiesCalculator();
        propertiesTool.printAllAverages(listOfLists_model);

        long single_analysis_end = System.currentTimeMillis();


        // --------------------------------------- MULTI-THREADED ANALYSIS ---------------------------------------
        long multi_analysis_start = System.currentTimeMillis();

        int threads = 2;

        ParallelGroupingTool parallelGroupingTool = new ParallelGroupingTool();
        // This method runs with multiple threads internally:
        List<List<EnrichedFlight>> listOfLists2_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, aircrafts, threads);
        List<List<EnrichedFlight>> listOfLists2_operator = parallelGroupingTool.groupFlightsByOperator(enrichedFlights, aircrafts, threads);
//
//        PropertiesCalculator propertiesTool2 = new PropertiesCalculator();


        long multi_analysis_end = System.currentTimeMillis();


        // Calculate durations
        long pre_analysis_duration = pre_analysis_end - pre_analysis_start;
        long single_analysis_duration = single_analysis_end - single_analysis_start;
        long multi_analysis_duration = multi_analysis_end - multi_analysis_start;

        // Print durations
        System.out.println("Pre-analysis duration: " + pre_analysis_duration);
        System.out.println("Single-threaded analysis duration: " + single_analysis_duration);
        System.out.println("Multi-threaded analysis duration: " + multi_analysis_duration);




    }
}
