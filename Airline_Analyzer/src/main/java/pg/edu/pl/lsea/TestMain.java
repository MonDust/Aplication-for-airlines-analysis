package pg.edu.pl.lsea;

import pg.edu.pl.lsea.data.engieniering.DataEnrichment;
import pg.edu.pl.lsea.data.engieniering.NullRemover;
import pg.edu.pl.lsea.files.CsvDataLoader;

/**
 * Main method for testing
 **/
public class TestMain {
    public static void main(String[] args) {
        System.out.println("Hello world");

        // Initialize - to uncomment when whole project will be together
        CsvDataLoader dataLoader = new CsvDataLoader();
        DataEnrichment dataEnrichment = new DataEnrichment();
        NullRemover nullRemover = new NullRemover();
        // CorrelationCalculator correlationCalculator = new CorrelationCalculator();

        // Load data
        // List<Flight> data = dataLoader.loadFlights();

        // Process with data engineering system
        //Object processedData = dataEnrichment.process(nullRemover.process(data));

        // Analyze data
        //Object analysisResult = correlationCalculator.analyze(processedData);
    }
}
