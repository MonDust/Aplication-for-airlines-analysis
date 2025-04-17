package pg.edu.pl.lsea;

/** Main method for testing **/
import pg.edu.pl.lsea.files.CsvDataReader;

import java.io.File;

/**
 * Main method for testing
 **/
public class TestMain {


    public static void main(String[] args) {

        // --------------------------------------- DATA LOADING ---------------------------------------

        long pre_analysis_start = System.currentTimeMillis();

        CsvDataReader dataLoader = new CsvDataReader();

        File fileAircrafts1 = new File("resources/aircraft-database-complete-2022-09.csv");
        File fileFlights1 = new File("resources/flight_sample_2022-09-26.csv");
        File fileFlights2 = new File("resources/flight_sample_2022-09-02.csv");
//        File fileFlights3 = new File("resources/flight_sample_2022-09-03.csv");
//        File fileFlights4 = new File("resources/flight_sample_2022-09-04.csv");
//        File fileFlights5 = new File("resources/flight_sample_2022-09-01.csv");

        // TODO - Replace with API requests to save read data. Optional, but we probably want to keep some logic in TestMain
//        dataLoader.loadFlightsToStorage(fileFlights1);
//        dataLoader.loadFlightsToStorage(fileFlights2);
//        dataLoader.loadFlightsToStorage(fileFlights3);
//        dataLoader.loadFlightsToStorage(fileFlights4);
//        dataLoader.loadFlightsToStorage(fileFlights5);


//        dataLoader.loadAircraftsToStorage(fileAircrafts1);


        long pre_analysis_end = System.currentTimeMillis();
        long pre_analysis_duration = pre_analysis_end - pre_analysis_start;
        System.out.println("Pre-analysis duration: " + pre_analysis_duration);



        // --------------------------------------- DATA ENGINEERING ---------------------------------------

        // TODO - Can be removed - we dont do data engineering on the client side
//        // Clean the data
//        nullRemover.TransformAircrafts(aircrafts);
//        nullRemover.TransformFlights(flights);
//        List<EnrichedFlight> enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flights);



        // --------------------------------------- ANALYSIS ---------------------------------------


        // TODO - Ask API to do some analysis based on already loaded data (aforementioned section) and print result to the console
    }
}
