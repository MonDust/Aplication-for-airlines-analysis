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
        sortAircraft(aircrafts);

        Collections.sort(aircrafts, new Aircraft.AircraftComparator());

        for (Aircraft aircraft : aircrafts) {
            System.out.println(aircraft);
        }


        System.out.println("\nSorted Flight List:");
        sortUsingComparator(flights);
     //  printFlightList(flights);
    }

    private void sortFlights(List<EnrichedFlight> flights) {
        Collections.sort(flights);
    }

    private void sortAircraft(List<Aircraft> aircrafts) {
        Collections.sort(aircrafts);
    }


    private void sortUsingComparator(List<EnrichedFlight> flights) {
        flights.sort(Comparator.comparingInt(EnrichedFlight::getTimeInAir));
    }

    private void printAircraftList(List<Aircraft> aircrafts) {
        for (Aircraft aircraft : aircrafts) {
            System.out.println(aircraft.getIcao24() + " " + aircraft.getModel());
        }
    }

    private void printFlightList(List<EnrichedFlight> flights) {
        for (EnrichedFlight flight : flights) {
            System.out.println("Flight: " + flight.getIcao24() + " | Time in Air: " + flight.getTimeInAir() + "s");
        }
    }
}

