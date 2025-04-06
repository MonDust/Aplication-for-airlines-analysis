package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;

public class ParallelGroupingTool {


    private List<String> giveListOfModels (List<Aircraft> aircrafts){

        List<String> output = new ArrayList<>();
        Set<String> seenModels = new HashSet<>();

        for (Aircraft aircraft : aircrafts) {
            String model = aircraft.getModel();
            if (model != null && seenModels.add(model)) {
                output.add(model);
            }
        }

        return output;
    }

    public List<List<EnrichedFlight>> sortFlightsByModel (List<EnrichedFlight> flights, List<Aircraft> aircrafts) {


        List<String> models = giveListOfModels(aircrafts);
        List<List<EnrichedFlight>> output = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(models.size());

        int threads = 2;

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
}
