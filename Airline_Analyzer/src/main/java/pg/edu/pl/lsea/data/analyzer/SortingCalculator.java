package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Output;

import java.util.*;

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
    }


    /**
     * Gives amount of flights per each ICAO
     * @param flights list of all flights
     * @return amount of flights per model writen in outpu objects
     */
    public List<Output> sortByAmountOfFlights ( List<EnrichedFlight> flights){

        List<Output> output = new ArrayList<>();

        Collections.sort(flights);

        String currentIcao = flights.getFirst().getIcao24();
        int Value = 0;

        for (EnrichedFlight flight : flights) {
            if (Objects.equals(flight.getIcao24(), currentIcao)){
                Value++;
            } else {
                System.out.println(currentIcao);
                System.out.println(Value);
                output.add(new Output(currentIcao, Value));
                Value = 0;
                currentIcao = flight.getIcao24();
            }
        }

        output.sort(Comparator.comparingInt(o -> o.Value));
        System.out.println(output);


        return output;
    }

    /**
     * Gives amount of time in air per each ICAO
     * @param flights list of all flights
     * @return in output object ICAO and it's time in air
     */
    public  List<Output>  sortByTimeOfFlights ( List<EnrichedFlight> flights){

        List<Output> output = new ArrayList<>();

        Collections.sort(flights);

        String currentIcao = flights.getFirst().getIcao24();
        int Value = 0;

        for (EnrichedFlight flight : flights) {
            if (Objects.equals(flight.getIcao24(), currentIcao)){
                Value += flight.getTimeInAir();
            } else {
                System.out.println(currentIcao);
                System.out.println(Value);
                output.add(new Output(currentIcao, Value));
                Value = 0;
                currentIcao = flight.getIcao24();
            }
        }

        output.sort(Comparator.comparingInt(o -> o.Value));
        System.out.println(output);


        return output;
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

    /**
     * function that returns top 'n' operators
     * @param ListOfListOprator list of lists of flights grouped by operator
     * @param n how much of top operators you want
     * @return n amount of operators from the top
     */
    public List<List<EnrichedFlight>> giveTopNOperators (List<List<EnrichedFlight>> ListOfListOprator, int n) {
        ListOfListOprator.sort(Comparator.comparingInt((List<EnrichedFlight> l) -> l.size()).reversed());
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            output.add(ListOfListOprator.get(i));
        }

        return output;
    }

}

