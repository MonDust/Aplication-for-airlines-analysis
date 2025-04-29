package pg.edu.pl.lsea.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Output;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataLoader {
    private final HttpClient client;
    private final ObjectMapper mapper;

    public DataLoader() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public record FlightResponse(String icao24, int firstSeen, int lastSeen, String departureAirport, String arrivalAirport) {}

    private Flight mapToFlight(FlightResponse response) {
        if (response == null) {
            return new Flight();
        }
        return new Flight(
                response.icao24,
                response.firstSeen,
                response.lastSeen,
                response.departureAirport,
                response.arrivalAirport
        );
    }

    public Flight getFlightByIcao(String icao) {
        String url = "http://localhost:8080/api/flights/" + icao;
        FlightResponse response = fetchFlightData(url);
        return mapToFlight(response);
    }

    public List<Flight> getAllFlights() {
        String url = "http://localhost:8080/api/flights";
        List<FlightResponse> responses = fetchFlightDataList(url);
        return responses.stream()
                .map(this::mapToFlight)
                .collect(Collectors.toList());
    }

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

            return mapper.readValue(response.body(), FlightResponse.class);
        } catch (Exception e) {
            System.out.println("Error fetching from: " + url);
            e.printStackTrace();
            return null;
        }
    }


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

            return mapper.readValue(response.body(),
                    mapper.getTypeFactory().constructCollectionType(List.class, FlightResponse.class));
        } catch (Exception e) {
            System.out.println("Error fetching from: " + url);
            e.printStackTrace();
            return null;
        }
    }


    public record AircraftResponse(Long id, String icao24, String model, String operator, String owner) {}

    private Aircraft mapToAircraft(AircraftResponse response) {
        if (response == null) {
            Aircraft aircraft = new Aircraft();
            aircraft.setNoInformation();
            return aircraft;
        }
        return new Aircraft(
                response.icao24(),
                response.model(),
                response.operator(),
                response.owner()
        );
    }

    public Aircraft getAircraftIcao(String icao) {
        String url = "http://localhost:8080/api/aircrafts/" + icao;
        AircraftResponse response = fetchAircraftData(url);
        return mapToAircraft(response);
    }

    public List<Aircraft> getAllAircrafts() {
        String url = "http://localhost:8080/api/aircrafts";
        List<AircraftResponse> responses = fetchAircraftDataList(url);
        return responses.stream()
                .map(this::mapToAircraft)
                .collect(Collectors.toList());
    }

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

            return mapper.readValue(response.body(), AircraftResponse.class);
        } catch (Exception e) {
            System.out.println("Error fetching from: " + url);
            e.printStackTrace();
            return null;
        }
    }

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

            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, AircraftResponse.class));
        } catch (Exception e) {
            System.out.println("Error fetching from: " + url);
            e.printStackTrace();
            return null;
        }
    }

    public List<Output> sortByAmountOfFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/sortByAmountOfFlights");
    }

    public List<Output> sortByTimeOfFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/sortByTimeOfFlights");
    }

    public List<Output> givePercentageOfLongFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/givePercentageOfLongFlights");
    }

    public List<Output> printAllAverages() {
        return fetchOutputList("http://localhost:8080/api/analysis/printAllAverages");
    }

    public List<List<EnrichedFlight>> findLongFlightsForEachModel() {
        return fetchLongFlightsList("http://localhost:8080/api/analysis/findLongFlightsForEachModel");
    }

    public List<Output> giveTopNOperators() {
        return fetchOutputList("http://localhost:8080/api/analysis/giveTopNOperators");
    }

    public List<Output> giveTopNOperators(int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/giveTopNOperators/" + howMuch);
    }

    public List<Output> giveTopNModels() {
        return fetchOutputList("http://localhost:8080/api/analysis/giveTopNModels");
    }

    public List<Output> giveTopNModels(int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/giveTopNModels/" + howMuch);
    }

    public List<Output> getTopNPercentageOfLongFlights() {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNPercentageOfLongFlights");
    }

    public List<Output> getTopNPercentageOfLongFlights(int howMuch) {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNPercentageOfLongFlights/" + howMuch);
    }

    public List<Output> getTopNAverageTime() {
        return fetchOutputList("http://localhost:8080/api/analysis/getTopNAverageTime");
    }

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

            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Output.class));
        } catch (Exception e) {
            System.out.println("Error fetching from: " + url);
            e.printStackTrace();
            return null;
        }
    }

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

            return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, mapper.getTypeFactory().constructCollectionType(List.class, EnrichedFlight.class)));
        } catch (Exception e) {
            System.out.println("Error fetching from: " + url);
            e.printStackTrace();
            return null;
        }
    }

    public Integer calculateAverageTimeInAir() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/analysis/calculateAverageTimeInAir"))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return Integer.parseInt(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
