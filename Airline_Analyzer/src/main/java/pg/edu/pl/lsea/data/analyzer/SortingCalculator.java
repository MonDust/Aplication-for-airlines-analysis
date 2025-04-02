package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.Comparator;
import java.util.List;

/**
 * This class performs all calculations conected with sorting nessesary for dashbord and other analysies
 */
public class SortingCalculator extends DataAnalyzer  {

    /**
     * Calculating sorted lists for dashboard
     * @param aircrafts list of aircraft meant to be analyzed
     * @param flights list of flights meant to be analyzed
     */
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


    /**
     * method responsible for printing list of aircrafts
     * @param aircrafts list of aircraft meant to be analyzed
     */
    private void printAircraftList(List<Aircraft> aircrafts) {
        for (Aircraft aircraft : aircrafts) {
            System.out.println(aircraft);
        }
    }

    /**
     * method responsible for printing list of flights
     * @param flights list of flights meant to be analyzed
     */
    private void printFlightList(List<EnrichedFlight> flights) {
        for (EnrichedFlight flight : flights) {
            System.out.println("Flight: " + flight.getIcao24() + " | Time in Air: " + flight.getTimeInAir() + "s");
        }
    }
}

