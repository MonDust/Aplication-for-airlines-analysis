package pg.edu.pl.lsea.files;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for reading CSV files with aircrafts and flights.
 */
public  class CsvDataReader extends FileDataReader {

    /**
     *
     * @param splitLine Line loaded as array of cell's contents from aircrafts csv file
     * @return Defines if the line is valid
     */
    private boolean validateAircraftLine(String[] splitLine) {

        if (splitLine.length < 14) {
            return false;
        }

        // Checks icao24 length
        return cleanString(splitLine[0]).length() == 6;
    }

    /**
     *
     * @param splitLine Line loaded as array of cell's contents from flights csv file
     * @return Defines if the line is valid
     */
    private boolean validateFlightLine(String[] splitLine) {
        if (splitLine.length != 9) {
            return false;
        }

        // Checks icao24 length
        return cleanString(splitLine[0]).length() == 6;
    }

    /**
     *
     * @param aircraftsFile Csv file with aircrafts which should be read
     */
    @Override
    public List<Aircraft> readAircrafts(File aircraftsFile) {
        List<Aircraft> aircrafts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(aircraftsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");

                if (validateAircraftLine(splitLine)) {
                    String icao24 = cleanString(splitLine[0].trim());

                    // Checks if first line is not a header
                    if (!icao24.equals("icao24")) {
                        String model = cleanString(splitLine[3].trim());
                        String operator = cleanString(splitLine[9].trim());
                        String owner = cleanString(splitLine[13].trim());

                        Aircraft aircraft = new Aircraft(icao24, model, operator, owner);
                        aircrafts.add(aircraft);
                    }
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return aircrafts;
    }

    /**
     *
     * @param flightsFile Csv file with flights which should be read
     */
    @Override
    public List<Flight> readFlights(File flightsFile) {
        List<Flight> flights = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(flightsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] splitLine = line.split(",");

                if (validateFlightLine(splitLine)) {
                    String icao24 = cleanString(splitLine[0]).trim();

                    // Checks if first line is not a header
                    if (!icao24.equals("icao24")) {
                        Flight flight = parseFlightSplitLine(splitLine, icao24);
                        flights.add(flight);
                    }
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return flights;
    }

    /**
     *
     * @param splitLine Line loaded as array of cell's contents from flights csv file
     * @param icao24 ICAO24 unique identifier of the flight
     * @return Flight created based on provided splitLine
     */
    private static Flight parseFlightSplitLine(String[] splitLine, String icao24) {
        String firstSeenString = splitLine[1].trim();
        int firstSeen = 0;
        if (!firstSeenString.isEmpty()) {
            try {
                firstSeen = Math.round(Float.parseFloat(firstSeenString));
            } catch (NumberFormatException e) {
                System.err.println("Invalid flight number: " + firstSeen);
            }
        }

        String lastSeenString = splitLine[2].trim();
        int lastSeen = 0;
        if (!lastSeenString.isEmpty()) {
            try {
                lastSeen = Math.round(Float.parseFloat(lastSeenString));
            } catch (NumberFormatException e) {
                System.err.println("Invalid flight number: " + lastSeenString);
            }
        }

        String airportDeparture = splitLine[4].trim();
        String airportArrival = splitLine[5].trim();

        return new Flight(icao24, firstSeen, lastSeen, airportDeparture, airportArrival);
    }

    /**
     * Removes quotation marks from string
     * @param text text to be cleaned
     * @return string without quotation marks
     */
    private static String cleanString(String text) {
        return text.replace("\"", "");
    }
}
