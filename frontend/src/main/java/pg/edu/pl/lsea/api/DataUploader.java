package pg.edu.pl.lsea.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DataUploader {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public DataUploader() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public void sendFlights(List<Flight> flights) {
        sendData(flights, "http://localhost:8080/api/flights/bulk", "flights");
    }

    public void sendAircrafts(List<Aircraft> aircrafts) {
        sendData(aircrafts, "http://localhost:8080/api/aircrafts/bulk", "aircrafts");
    }

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
//            System.out.println("Response body: " + response.body());


        } catch (Exception e) {
            System.out.println("Failed to send " + dataType + ":");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
