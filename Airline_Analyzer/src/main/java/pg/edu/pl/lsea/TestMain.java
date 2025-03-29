package pg.edu.pl.lsea;

/** Main method for testing **/
import pg.edu.pl.lsea.data.analyzer.SortingCaluclator;
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
        System.out.println("Hello world");

        // Initialize - to uncomment when whole project will be together
        // CsvDataLoader dataLoader = new CsvDataLoader();
        // DataAnalyzer dataAnalyzer = new DataAnalyzer();
        // DataCleaner dataCleaner = new DataCleaner();
        // FeatureBuilder featureBuilder = new FeatureBuilder();
        CsvDataLoader dataLoader = new CsvDataLoader();
        DataEnrichment dataEnrichment = new DataEnrichment();
        NullRemover nullRemover = new NullRemover();

        SortingCaluclator correlationCalculator = new SortingCaluclator();


        File fileFlights1 = new File("resources/flight_sample_2022-09-26.csv");
        File fileFlights2 = new File("resources/flight_sample_2022-09-02.csv");
        File fileFlights3 = new File("resources/flight_sample_2022-09-03.csv");
        File fileFlights4 = new File("resources/flight_sample_2022-09-04.csv");
        File fileFlights5 = new File("resources/flight_sample_2022-09-26.csv");
//        File fileAircrafts = new File("resources/aircraft-database-complete-2022-09.csv");
        // Load data
        //Object data = dataLoader.loadFlights();
//        long start = System.currentTimeMillis();
//        long end = System.currentTimeMillis();
//        System.out.println(end-start);
//        List<Flight> flights = dataLoader.readFlights(fileFlights);
//        List<Aircraft> aircrafts = dataLoader.readAircrafts(fileAircrafts);
//
//        System.out.println(aircrafts.size());
//        System.out.println(flights.size());
        long start = System.currentTimeMillis();
        dataLoader.loadFlightsToStorage(fileFlights1);
        long end = System.currentTimeMillis();
        System.out.println(end-start);

//        System.out.println(DataStorage.getInstance().countFlights());


        // Process with data engineering system
        //Object processedData = dataEnrichment.process(nullRemover.process(data));

        // Analyze data
        //Object analysisResult = correlationCalculator.analyze(processedData);
    }
}
