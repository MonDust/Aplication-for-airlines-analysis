package pg.edu.pl.lsea;

import com.fasterxml.jackson.databind.ObjectMapper;
import pg.edu.pl.lsea.api.DataLoader;
import pg.edu.pl.lsea.api.DataUploader;
import pg.edu.pl.lsea.api.dto.FlightResponse;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataReader;
import pg.edu.pl.lsea.udp.UdpClient;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Main method for testing
 **/
public class TestMain {


    public static void main(String[] args) {

        // --------------------------------------- DATA LOADING ---------------------------------------



        CsvDataReader dataLoader = new CsvDataReader();
        DataUploader uploader = new DataUploader();

        // Efficiency measurements
        Runtime runtime = Runtime.getRuntime();


        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        long beforeTime = System.currentTimeMillis();
        long beforeTotalMemory = runtime.getRuntime().totalMemory();
        long beforeFreeMemory = runtime.getRuntime().freeMemory();
        long beforeUsedMemory = beforeTotalMemory - beforeFreeMemory;
        long MaxMemory = runtime.maxMemory();

        File fileAircrafts1 = new File("resources/aircraft-database-complete-2022-09.csv");
        //File fileFlights1 = new File("resources/flight_sample_2022-09-26.csv");
        //File fileFlights2 = new File("resources/flight_sample_2022-09-02.csv");
        //File fileFlights3 = new File("resources/flight_sample_2022-09-03.csv");
        //File fileFlights4 = new File("resources/flight_sample_2022-09-04.csv");
        //File fileFlights5 = new File("resources/flight_sample_2022-09-01.csv");

        //File fileFlights1 = new File("resources/flight_sample_2022-09-04.csv");
        //File fileFlights2 = new File("resources/flight_sample_2022-09-25.csv");
        //File fileFlights3 = new File("resources/flight_sample_2022-09-03_1.csv");
        //File fileFlights4 = new File("resources/flight_sample_2022-09-03_2.csv");
        File fileFlights5 = new File("resources/flight_sample_2022-09-03_3.csv");

        // Could be used for debugging if file loading doesn't work
//        List<Flight> flights = new ArrayList<>();
//        flights.add(new Flight("abc123", 1682000000, 1682003600, "JFK", "LAX"));
//        flights.add(new Flight("def456", 1682007200, 1682010800, "ORD", "ATL"));


        // Send the list of flights and aircrafts via the REST API
        try {


            uploader.sendAircrafts(dataLoader.readAircrafts(fileAircrafts1));
            //uploader.sendFlights(dataLoader.readFlights(fileFlights1));
            //uploader.sendFlights(dataLoader.readFlights(fileFlights2));
            //uploader.sendFlights(dataLoader.readFlights(fileFlights3));
            //uploader.sendFlights(dataLoader.readFlights(fileFlights4));
            uploader.sendFlights(dataLoader.readFlights(fileFlights5));

        } catch (Exception e) {
            System.out.println("An error occurred while uploading flights.");
            System.out.println("Error Message: " + e.getMessage());
            System.out.println("Stack Trace:");
            e.printStackTrace();
        }


        long afterTime = System.currentTimeMillis();


        long differenceTime = afterTime - beforeTime;
        long afterTotalMemory = runtime.getRuntime().totalMemory();
        long afterFreeMemory = runtime.getRuntime().freeMemory();
        long afterUsedMemory = afterTotalMemory - afterFreeMemory;
        long differenceUsedMemory = afterUsedMemory-beforeUsedMemory;
        System.out.println("Pre-analysis duration: " + (differenceTime / 1000.0) + "s");
        System.out.println("Pre-analysis memory usage: " + (differenceUsedMemory / 1024.0 / 1024.0) + "MB");
        System.out.println("Pre-analysis memory limit: " + (MaxMemory) / 1024.0 / 1024.0 + "MB");


        // --------------------------------------- DATA ENGINEERING ---------------------------------------

        // TODO - Can be removed - we don't do data engineering on the client side
//        // Clean the data
//        NullRemover nullRemover = new NullRemover();
//        nullRemover.TransformAircrafts(aircrafts);
//        nullRemover.TransformFlights(flights);
//        List<EnrichedFlight> enrichedFlights = dataEnrichment.CreateEnrichedListOfFlights(flights);


        // --------------------------------------- ANALYSIS ---------------------------------------
        /*
        System.out.println("\n--- Starting analysis ---");
        String[] endpoints = {
                "getTopNModels",
                "getTopNOperators",
                "getTopNPercentageOfLongFlights",
                "getTopNAverageTime",
                "calculateAverageTimeInAir"
        };

        for (String endpoint : endpoints) {
            try {
                String fullUrl = "http://localhost:8080/api/analysis/" + endpoint;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .GET()
                        .build();






                beforeTime = System.currentTimeMillis();
                beforeTotalMemory = runtime.getRuntime().totalMemory();
                beforeFreeMemory = runtime.getRuntime().freeMemory();
                beforeUsedMemory = beforeTotalMemory - beforeFreeMemory;
                MaxMemory = runtime.maxMemory();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                afterTime = System.currentTimeMillis();

                differenceTime = afterTime - beforeTime;
                afterTotalMemory = runtime.getRuntime().totalMemory();
                afterFreeMemory = runtime.getRuntime().freeMemory();
                afterUsedMemory = afterTotalMemory - afterFreeMemory;
                differenceUsedMemory = afterUsedMemory - beforeUsedMemory;

                System.out.println("Endpoint: /" + endpoint);
                System.out.println("Response: " + response.body());
                System.out.println("Duration: " + (differenceTime / 1000.0) + "s");
                System.out.println("Memory usage: " + (differenceUsedMemory / 1024.0 / 1024.0) + "MB");
                System.out.println("Memory limit: " + (MaxMemory) / 1024.0 / 1024.0 + "MB");

            } catch (Exception e) {
                System.out.println("Error calling endpoint: " + endpoint);
                e.printStackTrace();
            }
        }
        */
        // --------------------------------------- UPDATE AND DELETE---------------------------------------
        /*
        System.out.println("\n--- Update ---");
        try {
            long flightId = 12968;
            String fullUrl = "http://localhost:8080/api/flights/" + flightId;

            FlightResponse updateRequest = new FlightResponse((long)12968, "aaaaaa", 0, 10, "exampleAirport", "exampleAirport");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(updateRequest)))
                    .build();


            beforeTime = System.currentTimeMillis();
            beforeTotalMemory = runtime.getRuntime().totalMemory();
            beforeFreeMemory = runtime.getRuntime().freeMemory();
            beforeUsedMemory = beforeTotalMemory - beforeFreeMemory;
            MaxMemory = runtime.maxMemory();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            afterTime = System.currentTimeMillis();

            differenceTime = afterTime - beforeTime;
            afterTotalMemory = runtime.getRuntime().totalMemory();
            afterFreeMemory = runtime.getRuntime().freeMemory();
            afterUsedMemory = afterTotalMemory - afterFreeMemory;
            differenceUsedMemory = afterUsedMemory - beforeUsedMemory;

            System.out.println("Endpoint: /flights/" + flightId);
            System.out.println("Response: " + response.body());
            System.out.println("Duration: " + (differenceTime / 1000.0) + "s");
            System.out.println("Memory usage: " + (differenceUsedMemory / 1024.0 / 1024.0) + "MB");
            System.out.println("Memory limit: " + (MaxMemory) / 1024.0 / 1024.0 + "MB");

        } catch (Exception e) {
            System.out.println("Error calling endpoint: /flights/");
            e.printStackTrace();
        }

        System.out.println("\n--- Delete one flight ---");
        try {
            long flightId = 12968;
            String fullUrl = "http://localhost:8080/api/flights/" + flightId;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .DELETE()
                    .build();


            beforeTime = System.currentTimeMillis();
            beforeTotalMemory = runtime.getRuntime().totalMemory();
            beforeFreeMemory = runtime.getRuntime().freeMemory();
            beforeUsedMemory = beforeTotalMemory - beforeFreeMemory;
            MaxMemory = runtime.maxMemory();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            afterTime = System.currentTimeMillis();

            differenceTime = afterTime - beforeTime;
            afterTotalMemory = runtime.getRuntime().totalMemory();
            afterFreeMemory = runtime.getRuntime().freeMemory();
            afterUsedMemory = afterTotalMemory - afterFreeMemory;
            differenceUsedMemory = afterUsedMemory - beforeUsedMemory;

            System.out.println("Endpoint: /flights/" + flightId);
            System.out.println("Response: " + response.body());
            System.out.println("Duration: " + (differenceTime / 1000.0) + "s");
            System.out.println("Memory usage: " + (differenceUsedMemory / 1024.0 / 1024.0) + "MB");
            System.out.println("Memory limit: " + (MaxMemory) / 1024.0 / 1024.0 + "MB");

        } catch (Exception e) {
            System.out.println("Error calling endpoint: /flights/");
            e.printStackTrace();
        }

        System.out.println("\n--- Delete by time range ---");
        try {
            int startDate = 1662159104;
            int endDate = 1662247040;
            String fullUrl = "http://localhost:8080/api/flights?start_date=" + startDate + "&end_date=" + endDate;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .DELETE()
                    .build();


            beforeTime = System.currentTimeMillis();
            beforeTotalMemory = runtime.getRuntime().totalMemory();
            beforeFreeMemory = runtime.getRuntime().freeMemory();
            beforeUsedMemory = beforeTotalMemory - beforeFreeMemory;
            MaxMemory = runtime.maxMemory();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            afterTime = System.currentTimeMillis();

            differenceTime = afterTime - beforeTime;
            afterTotalMemory = runtime.getRuntime().totalMemory();
            afterFreeMemory = runtime.getRuntime().freeMemory();
            afterUsedMemory = afterTotalMemory - afterFreeMemory;
            differenceUsedMemory = afterUsedMemory - beforeUsedMemory;

            System.out.println("Endpoint: /flights?start_date=" +startDate +"&end_date=" + endDate);
            System.out.println("Response: " + response.body());
            System.out.println("Duration: " + (differenceTime / 1000.0) + "s");
            System.out.println("Memory usage: " + (differenceUsedMemory / 1024.0 / 1024.0) + "MB");
            System.out.println("Memory limit: " + (MaxMemory) / 1024.0 / 1024.0 + "MB");

        } catch (Exception e) {
            System.out.println("Error calling endpoint: /flights/");
            e.printStackTrace();
        }
         */
        // TODO - Ask API to do some analysis based on already loaded data (aforementioned section) and print result to the console
//        DataLoader dataLoader1 = new DataLoader();
//        dataLoader1.giveTopNModels();

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
