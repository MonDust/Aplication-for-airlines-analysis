package pg.edu.pl.lsea.backend.data.analyzer.grouping.multithreading;

import pg.edu.pl.lsea.backend.data.analyzer.grouping.BaseGroupingAnalyzer;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Class designed to be run in parallel (as it implements Runnable interface)
 * Its goal is to save all flights for a given operator to a shared (across threads) list
 */
public class GetAllFlightsForOperatorTask extends BaseGroupingAnalyzer implements Runnable {
    private final List<EnrichedFlight> flights;
    private final List<Aircraft> aircrafts;
    private final String operator;
    /**
     * shared list across multiple threads
     */
    private final List<List<EnrichedFlight>> sharedResult;
    /**
     * CountDownLatch is used for tracking task completion across threads
     */
    CountDownLatch latch;

    /**
     * Creates a GetAllFlightsForOperatorTask object
     * @param flights list of flights
     * @param aircrafts list of aircraft
     * @param operator aircraft's operator
     * @param sharedResult shared list that contains the results of task completion
     * @param latch an instance of CountDownLatch
     */
    public GetAllFlightsForOperatorTask(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String operator, List<List<EnrichedFlight>> sharedResult, CountDownLatch latch){
        this.flights = flights;
        this.aircrafts = aircrafts;
        this.operator = operator;
        this.sharedResult = sharedResult;
        this.latch = latch;
    }

    /**
     * This method is run in parallel and contains the main class functionality.
     * It contains a critical section that is wrapped with synchronized block.
     */
    @Override
    public void run() {
        try {
            List<EnrichedFlight> result = getAllFlightsForOperator(flights, aircrafts, operator);
            synchronized (sharedResult) {
                sharedResult.add(result);
            }
        } finally {
            latch.countDown();
        }
    }
}
