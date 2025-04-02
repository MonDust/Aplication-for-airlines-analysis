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
import java.util.HashSet;
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


        List<Flight> flights = DataStorage.getInstance().getFlights();
        System.out.println(flights.stream().findAny());


        // Process with data engineering system
        //Object processedData = dataEnrichment.process(nullRemover.process(data));

        // Analyze data
        //Object analysisResult = correlationCalculator.analyze(processedData);
    }
}
