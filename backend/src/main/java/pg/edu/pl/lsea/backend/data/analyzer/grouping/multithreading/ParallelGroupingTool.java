package pg.edu.pl.lsea.backend.data.analyzer.grouping.multithreading;

import pg.edu.pl.lsea.backend.data.analyzer.grouping.BaseGroupingAnalyzer;
import pg.edu.pl.lsea.backend.entities.original.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.udp.UdpServer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

import static pg.edu.pl.lsea.backend.utils.Constants.NUMBER_OF_ROWS_TO_SEND_PROGRESS;

/**
 * Class designed for grouping data in parallel (multiple threads).
 */
public class ParallelGroupingTool extends BaseGroupingAnalyzer {
    /**
     * Default constructor for the class.
     */
    public ParallelGroupingTool() {}

    /**
     * Group flights by models in parallel by combining flights and aircraft with icao24 (the shared property)
     * @param flights list of flights
     * @param aircrafts list of aircraft
     * @param threads number of threads used for parallel grouping
     * @return list containing a list of flights for each model
     */
    public List<List<EnrichedFlight>> groupFlightsByModel (List<EnrichedFlight> flights, List<Aircraft> aircrafts, int threads) {

        List<String> models = getUniqueModels(aircrafts);
        CountDownLatch latch = new CountDownLatch(models.size());

        try (ExecutorService service = Executors.newFixedThreadPool(threads)) {
            List<List<EnrichedFlight>> sharedResult = new ArrayList<>();
            int processed = 0;
            int lastSent = 0;
            int total = models.size();
            for(String model: models) {
                processed++;
                service.submit(new GetAllFlightsForModelTask(flights, aircrafts, model, sharedResult, latch));

                // Update progress
                if (processed % NUMBER_OF_ROWS_TO_SEND_PROGRESS == 0 || processed == total) {
                    int delta = processed - lastSent;
                    // Thread.sleep(100);
                    UdpServer.sendProgress(delta, total);
                    lastSent = processed;
                }
            }
            // Final update in case something was missed
            int remaining = total - lastSent;
            if (remaining > 0) {
                UdpServer.sendProgress(remaining, total);
            }
            // Wait for everything to finish
            latch.await();

            return sharedResult;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while waiting for tasks to finish", e);
        }
    }

    /**
     * Group flights by operators in parallel by combining flights and aircraft with icao24 (the shared property)
     * @param flights list of flights
     * @param aircrafts list of aircraft
     * @param threads number of threads used for parallel grouping
     * @return list containing a list of flights for each operator
     */
    public List<List<EnrichedFlight>> groupFlightsByOperator (List<EnrichedFlight> flights, List<Aircraft> aircrafts, int threads) {

        List<String> operators = getUniqueOperators(aircrafts);
        CountDownLatch latch = new CountDownLatch(operators.size());

        try (ExecutorService service = Executors.newFixedThreadPool(threads)) {
            List<List<EnrichedFlight>> sharedResult = new ArrayList<>();

            int processed = 0;
            int lastSent = 0;
            int total = operators.size();

            for(String operator: operators) {
                processed++;
                service.submit(new GetAllFlightsForOperatorTask(flights, aircrafts, operator, sharedResult, latch));

                // Update progress
                if (processed % NUMBER_OF_ROWS_TO_SEND_PROGRESS == 0 || processed == total) {
                    int delta = processed - lastSent;
                    //Thread.sleep(100);
                    UdpServer.sendProgress(delta, total);
                    lastSent = processed;
                }
            }

            // Last update in case something was missed
            int remaining = total - lastSent;
            if (remaining > 0) {
                UdpServer.sendProgress(remaining, total);
            }

            // Wait for everything to finish
            latch.await();

            return sharedResult;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while waiting for tasks to finish", e);
        }
    }
}
