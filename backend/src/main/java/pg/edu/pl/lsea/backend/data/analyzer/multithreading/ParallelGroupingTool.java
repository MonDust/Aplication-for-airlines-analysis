package pg.edu.pl.lsea.backend.data.analyzer.multithreading;

import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

/**
 * Class designed for grouping data in parallel (multiple threads)
 */
public class ParallelGroupingTool {

    /**
     * Get all unique models from a list of aircraft
     * @param aircrafts list of aircraft
     * @return list of unique models
     */
    private List<String> getUniqueModels (List<Aircraft> aircrafts){

        List<String> result = new ArrayList<>();
        Set<String> seenModels = new HashSet<>();

        for (Aircraft aircraft : aircrafts) {
            String model = aircraft.getModel();
            if (model != null && seenModels.add(model)) {
                result.add(model);
            }
        }

        return result;
    }


    /**
     * Get all unique operators from a list of aircraft
     * @param aircrafts list of aircraft
     * @return list of unique operators
     */
    private List<String> getUniqueOperators (List<Aircraft> aircrafts){

        List<String> result = new ArrayList<>();
        Set<String> seenOperators = new HashSet<>();

        for (Aircraft aircraft : aircrafts) {
            String operator = aircraft.getOperator();
            if (operator != null && seenOperators.add(operator)) {
                result.add(operator);
            }
        }

        return result;
    }

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
            for(String model: models) {
                service.submit(new GetAllFlightsForModelTask(flights, aircrafts, model, sharedResult, latch));
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
            for(String operator: operators) {
                service.submit(new GetAllFlightsForOperatorTask(flights, aircrafts, operator, sharedResult, latch));
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
