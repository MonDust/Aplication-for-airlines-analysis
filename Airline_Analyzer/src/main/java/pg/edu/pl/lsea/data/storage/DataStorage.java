package pg.edu.pl.lsea.data.storage;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.entities.Aircraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Usage:
 * 1. use DataStorage.getInstance() to access the single instance
 * 2. add the data - flights and/or aircrafts one by one, duplicates are not added
 * 3. finalize data (convert to list) - not reversible during runtime
 * 4. data can be accessed for sorting/analysis
 * all data can be removed or counted at any point - before or after finalization
 */
public class DataStorage {
    private static final DataStorage INSTANCE = new DataStorage();
    private HashSet<Flight> flightsSet = new HashSet<>();
    private HashSet<Aircraft> aircraftsSet = new HashSet<>();

    private List<Flight> flights = null;
    private List<Aircraft> aircrafts = null;

    private boolean finalized = false;

    private DataStorage() {}

     public static DataStorage getInstance() {
        return INSTANCE;
    }

    /**
     * Does not add duplicate flights
     * @param flight The flight to be added to data storage.
     * @throws IllegalStateException if data is already finalized
     */
    public void addFlight(Flight flight) {
        if (finalized) {
            throw new IllegalStateException("Cannot add new flights after finalization.");
        }
        flightsSet.add(flight);
    }

    /**
     * Does not add duplicate aircrafts
     * @param aircraft The aircraft to be added to data storage.
     * @throws IllegalStateException if data is already finalized
     */
    public void addAircraft(Aircraft aircraft) {
        if (finalized) {
            throw new IllegalStateException("Cannot add new aircrafts after finalization.");
        }
        aircraftsSet.add(aircraft);
    }
    public List<Flight> getFlights() {
        if (!finalized) {
            throw new IllegalStateException("Data has to be finalized");
        }
        return flights;
    }
    public List<Aircraft> getAircrafts() {
        if (!finalized) {
            throw new IllegalStateException("Data has to be finalized");
        }
        return aircrafts;
    }
    public void deleteFlights() {
        if (!finalized) {
            flightsSet.clear();
        } else {
            flights.clear();
        }
    }
    public void deleteAircrafts() {
        if (!finalized) {
            aircraftsSet.clear();
        } else {
            aircrafts.clear();
        }
    }
    public int countFlights() {
        if (!finalized) return flightsSet.size();
        else return flights.size();
    }
    public int countAircrafts() {
        if (!finalized) return aircraftsSet.size();
        else return aircrafts.size();
    }
    public void finalizeData() {
        if (finalized) {
            throw new IllegalStateException("Data already finalized!");
        }
        flights = new ArrayList<>(flightsSet);
        aircrafts = new ArrayList<>(aircraftsSet);
        flightsSet.clear();
        flightsSet = null;
        aircraftsSet.clear();
        aircraftsSet = null;

        finalized = true;
    }
}
