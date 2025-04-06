package pg.edu.pl.lsea.data.analyzer.multithreading;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

public class ParallelGroupingTool {


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

    public List<List<EnrichedFlight>> groupFlightsByOperator (List<EnrichedFlight> flights, List<Aircraft> aircrafts, int threads) {

        List<String> operators = getUniqueOperators(aircrafts);
        CountDownLatch latch = new CountDownLatch(operators.size());

        try (ExecutorService service = Executors.newFixedThreadPool(threads)) {
            List<List<EnrichedFlight>> sharedResult = new ArrayList<>();
            for(String model: operators) {
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


}
