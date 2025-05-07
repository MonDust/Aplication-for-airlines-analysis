package pg.edu.pl.lsea.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.edu.pl.lsea.api.dto.AircraftResponse;
import pg.edu.pl.lsea.api.dto.FlightResponse;
import pg.edu.pl.lsea.api.dto.mapper.ResponseToAircraftMapper;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Output;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pg.edu.pl.lsea.api.dto.mapper.ResponseToFlightMapper;

import static pg.edu.pl.lsea.api.dto.mapper.ResponseToAircraftMapper.mapToAircraft;
import static pg.edu.pl.lsea.api.dto.mapper.ResponseToFlightMapper.mapToFlight;

/**
 * This class is responsible for loading flight and aircraft data from an API.
 * It supports performing various analysis operations related to flights and aircraft.
 */
public class DataLoader {
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
     * Adds HTTP client and mapper for JSON mapping.
     */
    public DataLoader() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Fetches a single flight by its ICAO code.
     * @param icao The ICAO code of the flight.
     * @return A Flight object corresponding to the ICAO code.
     */
    public Flight getFlightByIcao(String icao) {
        String url = "http://localhost:8080/api/flights/" + icao;
        FlightResponse response = fetchFlightData(url);
        return mapToFlight(response);
    }

    /**
     * Fetches all flights.
     * @return A list of all Flight objects.
     */
    public List<Flight> getAllFlights() {
        String url = "http://localhost:8080/api/flights";
        List<FlightResponse> responses = fetchFlightDataList(url);

        if (responses == null) {
            return Collections.emptyList();
        }
        return responses.stream()
                .map(ResponseToFlightMapper::mapToFlight)
                .collect(Collectors.toList());
    }


    /**
     * Fetches data for a single flight.
     * @param url The URL to fetch flight data from.
     * @return A FlightResponse object representing the flight data.
     */
    private FlightResponse fetchFlightData(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("GET " + url + " -> " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Non-200 response: " + response.body());
                return null;
            }

            if (response.body() == null || response.body().trim().isEmpty()) {
                System.out.println("No data returned from: " + url);
                return null;
            }

            try {
                return mapper.readValue(response.body(), FlightResponse.class);
            } catch (IOException e) {
                System.err.println("Error parsing response body: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }

    /**
     * Fetches a list of flights.
     * @param url The URL to fetch flight data from.
     * @return A list of FlightResponse objects.
     */
    private List<FlightResponse> fetchFlightDataList(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("GET " + url + " -> " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Non-200 response: " + response.body());
                return null;
            }

            if (response.body() == null || response.body().trim().isEmpty()) {
                System.out.println("No data returned from: " + url);
                return null;
            }

            try {
                return mapper.readValue(response.body(),
                        mapper.getTypeFactory().constructCollectionType(List.class, FlightResponse.class));
            } catch (IOException e) {
                System.err.println("Error parsing response body: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());

            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }

    /**
     * Fetches a single aircraft by its ICAO code.
     * @param icao The ICAO code of the aircraft.
     * @return An Aircraft object corresponding to the ICAO code.
     */
    public Aircraft getAircraftIcao(String icao) {
        String url = "http://localhost:8080/api/aircrafts/" + icao;
        AircraftResponse response = fetchAircraftData(url);
        return mapToAircraft(response);
    }

    /**
     * Fetches all aircraft.
     * @return A list of all Aircraft objects.
     */
    public List<Aircraft> getAllAircrafts() {
        String url = "http://localhost:8080/api/aircrafts";
        List<AircraftResponse> responses = fetchAircraftDataList(url);

        if (responses == null) {
            return Collections.emptyList();
        }

        return responses.stream()
                .map(ResponseToAircraftMapper::mapToAircraft)
                .collect(Collectors.toList());
    }

    /**
     * Fetches data for a single aircraft.
     * @param url The URL to fetch aircraft data from.
     * @return An AircraftResponse object representing the aircraft data.
     */
    private AircraftResponse fetchAircraftData(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("GET " + url + " -> " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Non-200 response: " + response.body());
                return null;
            }

            if (response.body() == null || response.body().trim().isEmpty()) {
                System.out.println("No data returned from: " + url);
                return null;
            }

            try {
                return mapper.readValue(response.body(), AircraftResponse.class);
            } catch (IOException e) {
                System.err.println("Error parsing response body: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }

    /**
     * Fetches a list of aircraft.
     * @param url The URL to fetch aircraft data from.
     * @return A list of AircraftResponse objects.
     */
    private List<AircraftResponse> fetchAircraftDataList(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("GET " + url + " -> " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Non-200 response: " + response.body());
                return null;
            }

            if (response.body() == null || response.body().trim().isEmpty()) {
                System.out.println("No data returned from: " + url);
                return null;
            }

            try {
                return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, AircraftResponse.class));
            } catch (IOException e) {
                System.err.println("Error parsing response body: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }

    /**
     * Retrieves a list of outputs sorted by the number of flights.
     * @return List of Output objects sorted by the number of flights.
     */
    public List<Output> sortByAmountOfFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/sortByAmountOfFlights");
    }

    /**
     * Retrieves a list of outputs sorted by the total time of flights.
     * @return List of Output objects sorted by total flight duration.
     */
    public List<Output> sortByTimeOfFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/sortByTimeOfFlights");
    }

    /**
     * Retrieves a list of outputs showing the percentage of long flights.
     * @return List of Output objects with long flight percentages.
     */
    public List<Output> givePercentageOfLongFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/givePercentageOfLongFlights");
    }


    /**
     * Retrieves all averages.
     * @return List of Output objects containing averages.
     */
    public List<Output> printAllAverages() {
        return fetchOutputList("http://localhost:8080/api/analysis/printAllAverages");
    }

    /**
     * Retrieves a list of long flights for each aircraft model.
     * @return List of lists of EnrichedFlight objects grouped by model.
     */
    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {
        return fetchLongFlightsList("http://localhost:8080/api/analysis/findLongFlightsForEachModel");
    }

    /**
     * Retrieves the top N operators by number of flights.
     * @return List of Output objects representing top operators.
     */
    public List<Output> getTopNOperators() {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNOperators");
    }

    /**
     * Retrieves the top N operators by number of flights.
     * @param howMuch Number of top operators to retrieve.
     * @return List of Output objects representing top operators.
     */
    public List<Output> getTopNOperators(int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNOperators/" + howMuch);
    }

    /**
     * Retrieves the top N aircraft models by number of flights.
     * @return List of Output objects representing top models.
     */
    public List<Output> getTopNModels() {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNModels");
    }

    /**
     * Retrieves the top N aircraft models by number of flights.
     * @param howMuch Number of top models to retrieve.
     * @return List of Output objects representing top models.
     */
    public List<Output> getTopNModels (int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNModels/" + howMuch);
    }

    /**
     * Retrieves the top N operators with their percentage of long flights.
     * @return List of Output objects representing the top percentages.
     */
    public List<Output> getTopNPercentageOfLongFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNPercentageOfLongFlights");
    }

    /**
     * Retrieves the top N operators with their percentage of long flights.
     * @param howMuch Number of top operators to retrieve.
     * @return List of Output objects representing the top percentages.
     */
    public List<Output> getTopNPercentageOfLongFlights(int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNPercentageOfLongFlights/" + howMuch);
    }

    /**
     * Retrieves the top operators and average flight time.
     * @return List of Output objects representing average time.
     */
    public List<Output> getTopNAverageTime() {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNAverageTime");
    }

    /**
     * Retrieves the top operators and average flight time.
     * @param howMuch Number of top entries to retrieve.
     * @return List of Output objects representing average time.
     */
    public List<Output> getTopNAverageTime(int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNAverageTime/" + howMuch);
    }

    /**
     * Sends a GET request to the given URL and returns a parsed list of Output objects.
     * @param url The endpoint to fetch the data from.
     * @return List of Output objects or null if the request fails.
     */
    private List<Output> fetchOutputList(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("GET " + url + " -> " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Non-200 response: " + response.body());
                return null;
            }

            if (response.body() == null || response.body().trim().isEmpty()) {
                System.out.println("No data returned from: " + url);
                return null;
            }

            try {
                return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Output.class));
            } catch (IOException e) {
                System.err.println("Error parsing response body: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }

    /**
     * Sends a GET request to the given URL and returns a parsed list of lists of EnrichedFlight objects.
     * @param url The endpoint to fetch the data from.
     * @return List of lists of EnrichedFlight objects or null if the request fails.
     */
    private List<List<EnrichedFlight>> fetchLongFlightsList(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("GET " + url + " -> " + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("Non-200 response: " + response.body());
                return null;
            }

            if (response.body() == null || response.body().trim().isEmpty()) {
                System.out.println("No data returned from: " + url);
                return null;
            }

            try {
                return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, mapper.getTypeFactory().constructCollectionType(List.class, EnrichedFlight.class)));
            } catch (IOException e) {
                System.err.println("Error parsing response body: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }

    /**
     * Retrieves calculated average time aircraft spent in the air.
     * @return Average flight time in minutes, or null if request fails.
     */
    public Integer calculateAverageTimeInAir() {
        String url = "http://localhost:8080/api/analysis/calculateAverageTimeInAir";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return Integer.parseInt(response.body());
        } catch (Exception e) {
            System.err.println("Error fetching from: " + url);
            System.err.println("Error: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("    at " + element);
            }
            return null;
        }
    }
}
