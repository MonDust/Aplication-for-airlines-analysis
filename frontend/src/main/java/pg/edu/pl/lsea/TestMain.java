package pg.edu.pl.lsea;

import pg.edu.pl.lsea.api.DataLoader;
import pg.edu.pl.lsea.api.DataUploader;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataReader;
import pg.edu.pl.lsea.udp.UdpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        File fileFlights3 = new File("resources/flight_sample_2022-09-03.csv");
        File fileFlights4 = new File("resources/flight_sample_2022-09-04.csv");
        File fileFlights5 = new File("resources/flight_sample_2022-09-01.csv");

        // Could be used for debugging if file loading doesn't work
//        List<Flight> flights = new ArrayList<>();
//        flights.add(new Flight("abc123", 1682000000, 1682003600, "JFK", "LAX"));
//        flights.add(new Flight("def456", 1682007200, 1682010800, "ORD", "ATL"));

        DataUploader uploader = new DataUploader();

        // Send the list of flights and aircrafts via the REST API
        try {
            uploader.sendAircrafts(dataLoader.readAircrafts(fileAircrafts1));

//            uploader.sendFlights(dataLoader.readFlights(fileFlights1));
//            uploader.sendFlights(dataLoader.readFlights(fileFlights2));
            uploader.sendFlights(dataLoader.readFlights(fileFlights3));

        } catch (Exception e) {
            System.out.println("An error occurred while uploading flights.");
            System.out.println("Error Message: " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        }

        long pre_analysis_end = System.currentTimeMillis();
        long pre_analysis_duration = pre_analysis_end - pre_analysis_start;
        System.out.println("Pre-analysis duration: " + pre_analysis_duration);



        // --------------------------------------- DATA ENGINEERING ---------------------------------------

        // TODO - Can be removed - we don't do data engineering on the client side
//        // Clean the data
//        NullRemover nullRemover = new NullRemover();
//        nullRemover.TransformAircrafts(aircrafts);
//        nullRemover.TransformFlights(flights);
//        List<EnrichedFlight> enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flights);



        // --------------------------------------- ANALYSIS ---------------------------------------


        // TODO - Ask API to do some analysis based on already loaded data (aforementioned section) and print result to the console
        DataLoader dataLoader1 = new DataLoader();
        dataLoader1.giveTopNOperators();

        // wait for analysis data
        Thread udpThread = new Thread(() -> {
            UdpClient.startListening();
        });

        // start thread
        udpThread.start();

        // wait for any progress to occur (without blocking main thread)
        while (UdpClient.getTotalProcessed() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // monitor progress
        while (UdpClient.getSumProcessed() < UdpClient.getTotalProcessed()) {
            int progress = (int) ((UdpClient.getSumProcessed() / (double) UdpClient.getTotalProcessed()) * 100);
                System.out.println(progress + "%");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // finished - stop listened
        UdpClient.stopListening();

        // optional: wait for udp to end
        try {
            udpThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
