package pg.edu.pl.lsea.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public  class CsvDataLoader extends FileDataLoader {
    // FIX - Change return type
    public void loadAircrafts(File aircraftFile) {
        try {
            Scanner myReader = new Scanner(aircraftFile);

            List<String> result = new ArrayList<>();
            for (int i = 100; i < 110 ; i++) {
                String line = myReader.nextLine();
                System.out.println(line);

                String[] splitLine = line.split(",");

                String icao24 = splitLine[0].trim();
                Integer firstSeen = Integer.parseInt(splitLine[1].trim());
                Integer lastSeen = Integer.parseInt(splitLine[2].trim());
                String airportDeparture = splitLine[4].trim();
                String airportArrival = splitLine[5].trim();

                result.add(icao24);
                result.add(firstSeen.toString());
                result.add(lastSeen.toString());
                result.add(airportDeparture);
                result.add(airportArrival);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void loadFlights(File flightsFile) {
        try {
            Scanner myReader = new Scanner(flightsFile);

            List<String> result = new ArrayList<>();
            for (int i = 0; i < 10 ; i++) {
                String line = myReader.nextLine();
                System.out.println(line);

                String[] splitLine = line.split(",");

                String icao24 = splitLine[0].trim();
                Integer firstSeen = Integer.parseInt(splitLine[1].trim());
                Integer lastSeen = Integer.parseInt(splitLine[2].trim());
                String airportDeparture = splitLine[4].trim();
                String airportArrival = splitLine[5].trim();

                result.add(icao24);
                result.add(firstSeen.toString());
                result.add(lastSeen.toString());
                result.add(airportDeparture);
                result.add(airportArrival);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
