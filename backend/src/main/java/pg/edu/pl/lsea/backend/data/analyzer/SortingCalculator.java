package pg.edu.pl.lsea.backend.data.analyzer;

import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Output;

import java.util.*;

/**
 * This class performs all calculations connected with sorting necessary for dashboard and other analysis.
 */
public class SortingCalculator extends DataAnalyzer  {

    /**
     * Calculating sorted lists for dashboard
     * @param aircrafts list of aircraft meant to be analyzed
     * @param flights list of flights meant to be analyzed
     */
    @Override
    public void analyzeDataForDashbord(List<Aircraft> aircrafts, List<EnrichedFlight> flights) {
        // sorted aircraft list
        aircrafts.sort(new Aircraft.AircraftComparator());
        printAircraftList(aircrafts);

        // sorted flight list
        flights.sort(Comparator.comparingInt(EnrichedFlight::getTimeInAir));
    }


    /**
     * Gives amount of flights per each ICAO24
     * @param flights list of all flights
     * @return list of flights per model written in output objects
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
                output.add(new Output(currentIcao, Value));
                Value = 0;
                currentIcao = flight.getIcao24();
            }
        }
        output.sort(Comparator.comparingInt(Output::getValue));
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
                output.add(new Output(currentIcao, Value));
                Value = 0;
                currentIcao = flight.getIcao24();
            }
        }

        output.sort(Comparator.comparingInt(Output::getValue));

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
     * Function that returns top 'n' operators/models
     * Works if already grouped by attribute.
     * @param ListOfList list of lists of flights grouped by certain category (operator/model)
     * @param n how much of top operators/models you want
     * @return n amount of operators/models from the top
     */
    public List<List<EnrichedFlight>> giveTopN_byAttribute(List<List<EnrichedFlight>> ListOfList, int n) {
        ListOfList.sort(Comparator.comparingInt((List<EnrichedFlight> l) -> l.size()).reversed());
        List<List<EnrichedFlight>> output = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            output.add(ListOfList.get(i));
        }
        return output;
    }

}

