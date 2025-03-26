package pg.edu.pl.lsea;

/** Main method for testing **/
import pg.edu.pl.lsea.data.analyzer.SortingCaluclator;
import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
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


        SortingCaluclator sortingCaluclator = new SortingCaluclator();


        File fileFlights = new File("src/resources/flight_sample_2022-09-26.csv");
        File fileAircrafts = new File("src/resources/aircraft-database-complete-2022-09.csv");
        // Load data
        //Object data = dataLoader.loadFlights();
         List<Aircraft> aircrafts = dataLoader.loadAircrafts(fileAircrafts);
         List<Flight> flights = dataLoader.loadFlights(fileFlights);

        nullRemover.TransformAll(aircrafts, flights);

        List<EnrichedFlight> enrichedFlights;
        enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flights);

        correlationCalculator.analyzeDataForDashbord(aircrafts, enrichedFlights);



        // Process with data engineering system
        //Object processedData = featureBuilder.process(dataCleaner.process(data));

        // Analyze data
        //Object analysisResult = dataAnalyzer.analyze(processedData);
    }
}
