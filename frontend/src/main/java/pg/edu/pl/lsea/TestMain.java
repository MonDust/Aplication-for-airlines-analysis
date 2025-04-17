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
import java.util.ArrayList;
import java.util.List;

/**
 * Main method for testing
 **/
public class TestMain {

    /**
     * This function performs analysis without writing anything to output.
     * It's meant to be used for using multiple times and measuring its execution time
     * @param threads number of threads that should be used in the intensive part of the analysis
     * @param enrichedFlights list of flights
     * @param aircrafts list of aircraft
     */
    public static void performAnalysis(int threads, List<EnrichedFlight> enrichedFlights, List<Aircraft> aircrafts) {

        // Sort the data
        SortingCalculator sortingCalculator = new SortingCalculator();


        // Firstly, group aircraft by models and store a list of flights for each model
        ParallelGroupingTool parallelGroupingTool = new ParallelGroupingTool();
        // This method runs with multiple threads internally:
        List<List<EnrichedFlight>> listOfLists_model = parallelGroupingTool.groupFlightsByModel(enrichedFlights, aircrafts, threads);
        List<List<EnrichedFlight>> listOfLists_operator = parallelGroupingTool.groupFlightsByOperator(enrichedFlights, aircrafts, threads);


        // Analysis
        GroupingTool groupingTool = new GroupingTool();
        List<List<EnrichedFlight>> longFlights = groupingTool.findLongFlightsForEachModel(listOfLists_model);

        // Secondly, for each model group, print average time in the air of all flights
        PropertiesCalculator propertiesTool = new PropertiesCalculator();
        propertiesTool.printAllAverages(listOfLists_model);
        propertiesTool.givePercentageOfLongFlights(listOfLists_model);


        sortingCalculator.giveTopNOperators(listOfLists_operator, 10);
        sortingCalculator.sortByAmountOfFlights(enrichedFlights);
        sortingCalculator.sortByTimeOfFlights(enrichedFlights);
    }

    public static void main(String[] args) {

        // --------------------------------------- DATA LOADING ---------------------------------------

        long pre_analysis_start = System.currentTimeMillis();

        CsvDataLoader dataLoader = new CsvDataLoader();
        DataEnrichment dataEnrichment = new DataEnrichment();
        NullRemover nullRemover = new NullRemover();

        File fileAircrafts1 = new File("resources/aircraft-database-complete-2022-09.csv");
        File fileFlights1 = new File("resources/flight_sample_2022-09-26.csv");
        File fileFlights2 = new File("resources/flight_sample_2022-09-02.csv");
//        File fileFlights3 = new File("resources/flight_sample_2022-09-03.csv");
//        File fileFlights4 = new File("resources/flight_sample_2022-09-04.csv");
//        File fileFlights5 = new File("resources/flight_sample_2022-09-01.csv");

        dataLoader.loadFlightsToStorage(fileFlights1);
        dataLoader.loadFlightsToStorage(fileFlights2);
//        dataLoader.loadFlightsToStorage(fileFlights3);
//        dataLoader.loadFlightsToStorage(fileFlights4);
//        dataLoader.loadFlightsToStorage(fileFlights5);


        dataLoader.loadAircraftsToStorage(fileAircrafts1);

        // Feature engineering (adding new features/properties to Flights)

        List<Aircraft> aircrafts = DataStorage.getInstance().getAircrafts();

        List<Flight> flights = DataStorage.getInstance().getFlights();

        long pre_analysis_end = System.currentTimeMillis();
        long pre_analysis_duration = pre_analysis_end - pre_analysis_start;
        System.out.println("Pre-analysis duration: " + pre_analysis_duration);



        // --------------------------------------- DATA ENGINEERING ---------------------------------------

        // Clean the data
        nullRemover.TransformAircrafts(aircrafts);
        nullRemover.TransformFlights(flights);
        List<EnrichedFlight> enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flights);



        // --------------------------------------- ANALYSIS ---------------------------------------
        // Values of this array are the number of threads for each analysis
        int[] threads_arr = {1, 2, 3, 4, 5, 10, 15, 20};

        // Number of points in the chart to be connected by a line
        int num_of_points = 5;

        // These intervals mean by how much we increase dataset size in each iteration:
        int interval_flights = enrichedFlights.size() / num_of_points;
        int remainder_flights = enrichedFlights.size() % num_of_points;
        int interval_aircrafts = aircrafts.size() / num_of_points;
        int remainder_aircrafts = aircrafts.size() % num_of_points;

        // This is for measuring execution time per threads
        long start_iteration, end_iteration;

        // Loop to get info for charts of execution times for given number of elements and for different number of threads
        // For each thread
        for (int threads: threads_arr) {
            // For each part of data (intervals: [0, interval_flights], [0, interval_flights*2], ..., [0, flights.size()])
            for (int i = 0; i < num_of_points; i++) {
                // "to" variable means how many elements from the array should we take into the analysis
                int to = (i == num_of_points - 1) ? flights.size() : (i + 1) * interval_flights;

                List<EnrichedFlight> subList = enrichedFlights.subList(0, to);

                start_iteration = System.currentTimeMillis();
                performAnalysis(threads, subList, aircrafts);
                end_iteration = System.currentTimeMillis();
                System.out.println("threads: " + threads + ", i: " + i + ", number of elements: " + to + ", time: " + (end_iteration - start_iteration));
            }
        }


        System.out.println("flights total size: " + flights.size());
        System.out.println("aircrafts total size: " + aircrafts.size());



    }
}
