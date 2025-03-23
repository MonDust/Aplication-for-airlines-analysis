package pg.edu.pl.lsea.files;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Class responsible for loading CSV files with aircrafts and flights.
 */
public  class CsvDataLoader extends FileDataLoader {

    /**
     *
     * @param splitLine Line loaded as array of cell's contents from aircrafts csv file
     * @return Defines if the line is valid
     */
    private boolean validateAircraftLine(String[] splitLine) {

        if (splitLine.length != 15) {
            return false;
        }

        // Checks icao24 length
        if (cleanString(splitLine[0]).length() != 6) {
            return false;
        }

        return true;
    };

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
        if (cleanString(splitLine[0]).length() != 6) {
            return false;
        }

        return true;
    };

    /**
     *
     * @param aircraftFile Csv file with aircrafts which should be read
     */
    public List<Aircraft> loadAircrafts(File aircraftFile) {
        try {
            Scanner myReader = new Scanner(aircraftFile);

            List<Aircraft> aircrafts = new ArrayList<>();
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();

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
            return aircrafts;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
//        try {
//            Scanner myReader = new Scanner(aircraftFile);
//
//            List<Aircraft> aircrafts = new ArrayList<>();
//            while (myReader.hasNextLine()) {
//                String line = myReader.nextLine();
//
//                String[] splitLine = line.split(",");
//
//                System.out.println(splitLine[0].length());
//                if (validateAircraftLine(splitLine)) {
//                    String icao24 = splitLine[0].trim();
//
//                    // Checks if first line is not a header
//                    if (!icao24.equals("icao24")) {
//                        String model = splitLine[3].trim();
//                        String operator = splitLine[9].trim();
//                        String owner = splitLine[13].trim();
//
//                        Aircraft aircraft = new Aircraft(icao24, model, operator, owner);
//                        aircrafts.add(aircraft);
//                    }
//                }
//            }
//
//            myReader.close();
//
//            return aircrafts;
//
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return null;
    }

    /**
     *
     * @param flightsFile Csv file with flights which should be read
     */
    public List<Flight> loadFlights(File flightsFile) {
        try {
            Scanner myReader = new Scanner(flightsFile);

            List<Flight> flights = new ArrayList<>();
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();

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

            myReader.close();

            return flights;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
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
            firstSeen = Math.round(Float.parseFloat(firstSeenString));
        }

        String lastSeenString = splitLine[2].trim();
        int lastSeen = 0;
        if (!lastSeenString.isEmpty()) {
            lastSeen = Math.round(Float.parseFloat(lastSeenString));
        }

        String airportDeparture = splitLine[4].trim();
        String airportArrival = splitLine[5].trim();

        Flight flight = new Flight(icao24, firstSeen, lastSeen, airportDeparture, airportArrival);
        return flight;
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
