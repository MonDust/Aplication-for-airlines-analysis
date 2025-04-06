package pg.edu.pl.lsea.data.analyzer.multithreading;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class GetAllFlightsForModelTask implements Runnable {
    private final List<EnrichedFlight> flights;
    private final List<Aircraft> aircrafts;
    private final String model;
    private final List<List<EnrichedFlight>> sharedResult;
    CountDownLatch latch;

    public GetAllFlightsForModelTask(List<EnrichedFlight> flights, List<Aircraft> aircrafts, String model, List<List<EnrichedFlight>> sharedResult, CountDownLatch latch){
        this.flights = flights;
        this.aircrafts = aircrafts;
        this.model = model;
        this.sharedResult = sharedResult;
        this.latch = latch;
    }

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
