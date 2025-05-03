package pg.edu.pl.lsea.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Class responsible for uploading flight and aircraft data
 * to a REST API endpoint using HTTP POST requests with JSON data.
 */
public class DataUploader {

    /**
     * HTTP client used to send requests to the backend API
     */
    private final HttpClient client;
    /**
     * Object mapper used to map objects to JSON
     */
    private final ObjectMapper mapper;

    /**
     * Constructor for the class.
     * Adds default HTTP client and JSON object mapper.
     */
    public DataUploader() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    /**
     * Sends a list of Flight objects to the flight upload API endpoint.
     * @param flights the list of flights to send
     */
    public void sendFlights(List<Flight> flights) {
        sendData(flights, "http://localhost:8080/api/flights/bulk", "flights");
    }

    /**
     * Sends a list of Aircraft objects to the aircraft upload API endpoint.
     * @param aircrafts the list of aircrafts to send
     */
    public void sendAircrafts(List<Aircraft> aircrafts) {
        sendData(aircrafts, "http://localhost:8080/api/aircrafts/bulk", "aircrafts");
    }

    /**
     * Sends a list of objects as JSON data to the specified URL via a POST request.
     * @param data the list of data objects to send
     * @param url the URL of the API endpoint
     * @param dataType a string representing the type of data being sent (used for logging)
     * @param <T> the type of data in the list
     */
    private <T> void sendData(List<T> data, String url, String dataType) {
        try {
            String json = mapper.writeValueAsString(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Sent " + dataType + ": " + data.size() + " items");
            System.out.println("Response code: " + response.statusCode());

        } catch (Exception e) {
            System.err.println("Failed to send " + dataType + ":");
            System.err.println("Error: " + e.getMessage());

            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
        }
    }
}
