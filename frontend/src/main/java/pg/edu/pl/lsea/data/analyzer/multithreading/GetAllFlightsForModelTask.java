package pg.edu.pl.lsea.data.analyzer.multithreading;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Class designed to be run in parallel (as it implements Runnable interface)
 * Its goal is to save all flights for a given model to a shared (across threads) list
 */
public class GetAllFlightsForModelTask implements Runnable {
    private final List<EnrichedFlight> flights;
    private final List<Aircraft> aircrafts;
    private final String model;
    /**
     * shared list across multiple threads
     */
    private final List<List<EnrichedFlight>> sharedResult;
    /**
     * CountDownLatch is used for tracking task completion across threads
     */
    CountDownLatch latch;

    /**
     * Creates a GetAllFlightsForModelTask object
     * @param flights list of flights
     * @param aircrafts list of aircraft
     * @param model aircraft's model
     * @param sharedResult shared list that contains the results of task completion
     * @param latch an instance of CountDownLatch
     */
    public GetAllFlightsForModelTask(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String model, List<List<EnrichedFlight>> sharedResult, CountDownLatch latch){
        this.flights = flights;
        this.aircrafts = aircrafts;
        this.model = model;
        this.sharedResult = sharedResult;
        this.latch = latch;
    }

    /**
     * This method is run in parallel and contains the main class functionality
     * It contains a critical section that is wrapped with synchronized block
     */
    @Override
    public void run() {
        try {
            List<EnrichedFlight> result = new ArrayList<>();

            List<String> icaoList = new ArrayList<>();

            for (Aircraft aircraft : aircrafts) {
                if (Objects.equals(aircraft.getModel(), model)) {
                    icaoList.add(aircraft.getIcao24());
                }
            }

            for (EnrichedFlight flight : flights) {
                for (String icao : icaoList) {
                    if (Objects.equals(flight.getIcao24(), icao)) {
                        result.add(flight);

                    }
                }
            }

            synchronized (sharedResult) {
                sharedResult.add(result);
            }
        } finally {
            latch.countDown();
        }
    }
}
