package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingCaluclator extends DataAnalyzer  {

    @Override
    public void analyzeDataForDashbord(List<Aircraft> aircrafts, List<EnrichedFlight> flights) {
        System.out.println("===== Dashboard =====");
        System.out.println("Sorted Aircraft List:");

        aircrafts.sort(new Aircraft.AircraftComparator());
        printAircraftList(aircrafts);

        System.out.println("\nSorted Flight List:");
        flights.sort(Comparator.comparingInt(EnrichedFlight::getTimeInAir));
        printFlightList(flights);
    }



    private void printAircraftList(List<Aircraft> aircrafts) {
        for (Aircraft aircraft : aircrafts) {
            System.out.println(aircraft);
        }
    }

    private void printFlightList(List<EnrichedFlight> flights) {
        for (EnrichedFlight flight : flights) {
            System.out.println("Flight: " + flight.getIcao24() + " | Time in Air: " + flight.getTimeInAir() + "s");
        }
    }
}

