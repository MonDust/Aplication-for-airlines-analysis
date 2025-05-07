package pg.edu.pl.lsea.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import pg.edu.pl.lsea.api.dto.FlightResponse;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class responsible for sending update and delete requests
 * to the backend API for Flight and Aircraft entities.
 */
public class DataUpdateDelete {
    private final HttpClient client;
    private final ObjectMapper mapper;

    /**
     * Class constructor.
     */
    public DataUpdateDelete() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    // --- UPDATE METHODS ---

    public void updateFlight(Long id, Flight flight) {
        sendPut("http://localhost:8080/api/flights/" + id, flight, "flight (PUT)");
    }

    public void patchFlight(Long id, Flight flight) {
        sendPatch("http://localhost:8080/api/flights/" + id, flight, "flight (PATCH)");
    }

    public void patchFlightByIcao24AndFirstSeen(String icao24, int firstSeen, Flight flight) {
        String url = "http://localhost:8080/api/flights/" + icao24 + "/" + firstSeen;
        sendPatch(url, flight, "flight (PATCH by icao24 and firstSeen)");
    }

    public void updateAircraft(String icao24, Aircraft aircraft) {
        sendPut("http://localhost:8080/api/aircrafts/" + icao24, aircraft, "aircraft (PUT)");
    }

    public void patchAircraft(String icao24, Aircraft aircraft) {
        sendPatch("http://localhost:8080/api/aircrafts/" + icao24, aircraft, "aircraft (PATCH)");
    }

    // --- DELETE METHODS ---

    public void deleteFlight(Long id) {
        sendDelete("http://localhost:8080/api/flights/" + id, "flight");
    }

    public void deleteFlightsByDateRange(int startDate, int endDate) {
        String url = "http://localhost:8080/api/flights?start_date=" + startDate + "&end_date=" + endDate;
        sendDelete(url, "flights (range)");
    }

    public void deleteAircraft(String icao24) {
        sendDelete("http://localhost:8080/api/aircrafts/" + icao24, "aircraft");
    }


    private <T> void sendPut(String url, T body, String type) {
        try {
            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("PUT " + type + ": " + response.statusCode());

        } catch (Exception e) {
            System.err.println("Failed PUT " + type + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private <T> void sendPatch(String url, T body, String type) {
        try {
            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("PATCH " + type + ": " + response.statusCode());

        } catch (Exception e) {
            System.err.println("Failed PATCH " + type + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendDelete(String url, String type) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DELETE " + type + ": " + response.statusCode());

        } catch (Exception e) {
            System.err.println("Failed DELETE " + type + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
