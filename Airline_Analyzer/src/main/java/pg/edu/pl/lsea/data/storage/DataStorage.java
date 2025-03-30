package pg.edu.pl.lsea.data.storage;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Aircraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A singleton class for storing all loaded trackable data - flights and aircrafts.
 * Implements adding data to storage row by row or in bulk and prevents the addition of duplicates by using a HashSet to store the newly added data.
 * If a user wants to load more data or restart loading from scratch the revertFinalization() function should be called to convert the data back to a HashSet().
 */
public class  DataStorage {
    /**
     * The only existing instance of the DataStorage class accessed by getInstance() method.
     */
    private static final DataStorage INSTANCE = new DataStorage();
    /**
     * HashSet for storing and loading flight data before finalization.
     */
    private HashSet<Flight> flightsSet = new HashSet<>();
    /**
     * HashSet for storing and loading aircraft data before finalization.
     */
    private HashSet<Aircraft> aircraftsSet = new HashSet<>();
    /**
     * A list for storing finalized flight data, can be used for sorting and analysis.
     */
    private List<Flight> flights = null;
    /**
     * A list for storing finalized aircraft data, can be used for sorting and analysis.
     */
    private List<Aircraft> aircrafts = null;

    /**
     * Checks if flights data is accessible as a list or if they should be converted from hashset
     */
    private boolean flightsAccessible = false;
    /**
     * Checks if aircrafts data is accessible as a list or if they should be converted from hashset
     */
    private boolean aircraftsAccessible = false;
    /**
     * An empty private constructor to prevent the creation of additional DataStorage instances.
     */
    private DataStorage() {}

    /**
     * A method that can be called to access the single instance of the DataStorage class.
     * @return the instance of the DataStorage class
     */
     public static DataStorage getInstance() {
        return INSTANCE;
    }

    /**
     * Allows for addition of the loaded flight data to storage (HashSet).
     * Data is added one flight object at a time.
     * Duplicates are not added.
     * Flights are considered equal if they have the same icao24 and firstSeen values.
     * @param flight the flight to be added to data storage
     * @throws IllegalStateException if data is already finalized
     */
    public void addFlight(Flight flight) {
        boolean contained = flightsSet.add(flight);

        if (!contained) {
            flightsAccessible = false;
        }
    }

    /**
     * Allows for addition of the loaded flights data to storage.
     * Duplicates are not added.
     * Flights are considered equal if they have the same icao24 and firstSeen values.
     * @param flights list of flights to be added to data storage
     */
    public void bulkAddFlights(List<Flight> flights) {
        boolean changed = flightsSet.addAll(flights);

        if (changed) {
            flightsAccessible = false;
        }
    }

    /**
     * Allows for addition of the loaded aircraft data to storage (HashSet).
     * Data is added one aircraft object at a time.
     * Duplicates are not added.
     * Aircrafts are considered equal if they have the same icao24 and model values.
     * @param aircraft the aircraft to be added to data storage
     * @throws IllegalStateException if data is already finalized
     */
    public void addAircraft(Aircraft aircraft) {
        boolean contained = aircraftsSet.add(aircraft);

        if (!contained) {
            aircraftsAccessible = false;
        }
    }

    /**
     * Allows for addition of the loaded aircrafts data to storage (HashSet).
     * Duplicates are not added.
     * Aircrafts are considered equal if they have the same icao24 and model values.
     * @param aircrafts list of aircrafts to be added to data storage
     */
    public void bulkAddAircrafts(List<Aircraft> aircrafts) {
        boolean changed = aircraftsSet.addAll(aircrafts);

        if (changed) {
            aircraftsAccessible = false;
        }
    }

    /**
     * @return A list containing all loaded flight data without duplicates.
     * @throws IllegalStateException if data has not been finalized
     */
    public List<Flight> getFlights() {
        convertFlightsAccessible();
        return flights;
    }
    /**
     * @return A list containing all loaded aircraft data without duplicates.
     * @throws IllegalStateException if data has not been finalized
     */
    public List<Aircraft> getAircrafts() {
        convertAircraftsAccessible();
        return aircrafts;
    }

    /**
     * Deletes all loaded flight data.
     */
    public void deleteFlights() {
        flightsSet.clear();
        flights.clear();
    }
    /**
     * Deletes all loaded aircraft data.
     */
    public void deleteAircrafts() {
        aircraftsSet.clear();
        aircrafts.clear();
    }

    /**
     * A method for counting the loaded rows of flight data.
     * @return An integer value - number of rows of flight data (number of flight objects added)
     */
    public int countFlights() {
        return flightsSet.size();
    }
    /**
     * A method for counting the loaded rows of aircraft data.
     * @return An integer value - number of rows of aircraft data (number of aircraft objects added)
     */
    public int countAircrafts() {
        return aircraftsSet.size();
    }

    /**
     * Converts flights data stored as HashSet to list so it can be accessed from outside.
     */
    private void convertFlightsAccessible() {
        if (!flightsAccessible) {
            flights = new ArrayList<>(flightsSet);
            flightsAccessible = true;
        }
    }
    /**
     * Converts aircrafts data stored as HashSet to list so it can be accessed from outside.
     */
    private void convertAircraftsAccessible() {
        if (!aircraftsAccessible) {
            aircrafts = new ArrayList<>(aircraftsSet);
            aircraftsAccessible = true;
        }
    }
}
