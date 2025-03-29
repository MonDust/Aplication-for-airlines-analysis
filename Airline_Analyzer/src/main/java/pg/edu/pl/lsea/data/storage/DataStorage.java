package pg.edu.pl.lsea.data.storage;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Aircraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A singleton class for storing all loaded trackable data - flights and aircrafts.
 * Implements adding data to storage row by row and prevents the addition of duplicates by using a HashSet to store the newly added data.
 * After data loading is finished the data should be finalized - converted to a list to allow sorting and further analysis.
 * If a user wants to load more data or restart loading from scratch the revertFinalization() function should be called to convert the data back to a HashSet().
 * Step by step usage:
 * 1. Use DataStorage.getInstance() to access the single instance.
 * 2. Add the data - flights and/or aircrafts one by one, duplicates are not added.
 * 3. After addition data can be counted or deleted.
 * 4. In order to access the data for further analysis finalize the data (convert to list).
 * 5. Finalized data can be accessed by getFlights() and getAircrafts() methods, can be counted or deleted, but cannot be added anymore.
 * 6. In order to add data again the revertFinalization() method should be called to convert the data back to a HashSet.
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
     * A boolean that checks if the data has been finalized by the finalizeData() method.
     * Finalized is stored as a list and can be accessed for analysis.
     * Data that is not finalized is stored as a HashSet and can be loaded by the addFlight() and addAircraft() methods.
     * Duplicate data is not added.
     */
    private boolean finalized = false;

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
     * Data can only be added if it has not been finalized (converted to a list).
     * Data is added one flight object at a time.
     * Duplicates are not added.
     * Flights are considered equal if they have the same icao24 and firstSeen values.
     * @param flight the flight to be added to data storage
     * @throws IllegalStateException if data is already finalized
     */
    public void addFlight(Flight flight) {
        if (finalized) {
            throw new IllegalStateException("Cannot add new flights after finalization.");
        }
        flightsSet.add(flight);
    }

    /**
     * Allows for addition of the loaded flights data to storage.
     * Duplicates are not added.
     * Flights are considered equal if they have the same icao24 and firstSeen values.
     * @param flights list of flights to be added to data storage
     */
    public void bulkAddFlights(List<Flight> flights) {
        if (!finalized) {
            finalizeData();
        }
        System.out.println(String.format("flights size: ", flights.size()));
        System.out.println(String.format("flightsSet size: ", flightsSet.size()));

        flights.forEach( flight -> flightsSet.add(flight));
//        flightsSet.addAll(flights);

        System.out.println(String.format("flights size: ", flights.size()));
        System.out.println(String.format("flightsSet size: ", flightsSet.size()));
        revertFinalization();


    }

    /**
     * Allows for addition of the loaded aircraft data to storage (HashSet).
     * Data can only be added if it has not been finalized (converted to a list).
     * Data is added one aircraft object at a time.
     * Duplicates are not added.
     * Aircrafts are considered equal if they have the same icao24 and model values.
     * @param aircraft the aircraft to be added to data storage
     * @throws IllegalStateException if data is already finalized
     */
    public void addAircraft(Aircraft aircraft) {
        if (finalized) {
            throw new IllegalStateException("Cannot add new aircrafts after finalization.");
        }
        aircraftsSet.add(aircraft);
    }

    /**
     * Allows for addition of the loaded aircrafts data to storage (HashSet).
     * Duplicates are not added.
     * Aircrafts are considered equal if they have the same icao24 and model values.
     * @param aircrafts list of aircrafts to be added to data storage
     */
    public void bulkAddAircrafts(List<Aircraft> aircrafts) {
        if (!finalized) {
            finalizeData();
        }

        aircraftsSet.addAll(aircrafts);

        revertFinalization();
    }

    /**
     * A method for accessing the loaded flight data once it has been finalized.
     * @return A list containing all loaded flight data without duplicates.
     * @throws IllegalStateException if data has not been finalized
     */
    public List<Flight> getFlights() {
        if (!finalized) {
            finalizeData();
        }
        return flights;
    }
    /**
     * A method for accessing the loaded aircraft data once it has been finalized.
     * @return A list containing all loaded aircraft data without duplicates.
     * @throws IllegalStateException if data has not been finalized
     */
    public List<Aircraft> getAircrafts() {
        if (!finalized) {
            finalizeData();
        }
        return aircrafts;
    }

    /**
     * Deletes all loaded flight data whether it has been finalized or not.
     * If used on finalized data the revertFinalization() method must be called before adding new data.
     */
    public void deleteFlights() {
        if (!finalized) {
            flightsSet.clear();
        } else {
            flights.clear();
        }
    }
    /**
     * Deletes all loaded aircraft data whether it has been finalized or not.
     * If used on finalized data the revertFinalization() method must be called before adding new data.
     */
    public void deleteAircrafts() {
        if (!finalized) {
            aircraftsSet.clear();
        } else {
            aircrafts.clear();
        }
    }

    /**
     * A method for counting the loaded rows of flight data. Can be used before and after finalization.
     * @return An integer value - number of rows of flight data (number of flight objects added)
     */
    public int countFlights() {
        if (!finalized) return flightsSet.size();
        else return flights.size();
    }
    /**
     * A method for counting the loaded rows of aircraft data. Can be used before and after finalization.
     * @return An integer value - number of rows of aircraft data (number of aircraft objects added)
     */
    public int countAircrafts() {
        if (!finalized) return aircraftsSet.size();
        else return aircrafts.size();
    }

    /**
     * A method for converting the added data (both aircrafts and flights at the same time) to a list.
     * Before finalization the data is stored as a HashSet - prevents duplication but keeps data unordered,
     * cannot be accessed for analysis.
     * After finalization the data is stored as a list - allows sorting and analysis of data, but data can no longer be added.
     * In order to add data after calling finalizeData() method the user has to use the revertFinalization() method
     *  - all data will be converted back to a HashSet
     *  Use this method only after all data for analysis has been added to maximize the efficiency.
     * @throws IllegalStateException if data is already finalized
     */
    private void finalizeData() {
        long start = System.currentTimeMillis();

        if (finalized) {
            throw new IllegalStateException("Data already finalized!");
        }
        flights = new ArrayList<>(flightsSet);
        aircrafts = new ArrayList<>(aircraftsSet);
        flightsSet.clear();
        aircraftsSet.clear();

        finalized = true;

        long end = System.currentTimeMillis();
//        System.out.println(String.format("Finalization: ", end-start));
    }

    /**
     * A method for converting all data (both aircrafts and flights) back to a HashSet after the finalizeData() method has been called.
     * After using this method data can be added again but can no longer be accessed for analysis.
     * @throws IllegalStateException if data has not been finalized
     */
    private void revertFinalization() {
        long start = System.currentTimeMillis();
        if (!finalized) {
            throw new IllegalStateException("Data not finalized!");
        }
        flightsSet = new HashSet<>(flights);
        aircraftsSet = new HashSet<>(aircrafts);
        flights.clear();
        aircrafts.clear();

        finalized = false;

        long end = System.currentTimeMillis();
//        System.out.println(String.format("Reverting finalization: ", end-start));
    }
}
