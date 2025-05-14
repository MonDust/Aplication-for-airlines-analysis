package pg.edu.pl.lsea.backend.data.analyzer;

import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.analysis.Output;
import pg.edu.pl.lsea.backend.udp.UdpServer;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.lsea.backend.utils.Constants.NUMBER_OF_ROWS_TO_SEND_PROGRESS;

/**
 * This clas calculates various properties connected with our data
 */
public class PropertiesCalculator{

    private static final int minimalTimeInAirForLongFlightSeconds = 1800;

    /**
     * Calculate pearson correlation for 2 float lists
     * @param List1 first list of float
     * @param List2 second list of float
     * @return correlation between two lists in float
     */
    public float calculateCorrelation(float[] List1, float[] List2) {

        float sx = 0.0F;
        float sy = 0.0F;
        float sxx = 0.0F;
        float syy = 0.0F;
        float sxy = 0.0F;

        int n = List1.length;

        for(int i = 0; i < n; ++i) {
            float x = List1[i];
            float y = List2[i];

            sx += x;
            sy += y;
            sxx += x * x;
            syy += y * y;
            sxy += x * y;
        }

        // covariation
        float cov = sxy / n - sx * sy / n / n;
        // standard error of x
        float sigmax = (float) Math.sqrt(sxx / n -  sx * sx / n / n);
        // standard error of y
        float sigmay = (float) Math.sqrt(syy / n -  sy * sy / n / n);

        // correlation is just a normalized covariation

        return cov / sigmax / sigmay;
    }

    /**
     * Calculates average time in air for list of flights
     * @param ListOfEnrichedFlights list of flights from with averred will be calculated
     * @return averred time per input flights
     */
    public int calculateAverageTimeInAir(List<EnrichedFlight> ListOfEnrichedFlights) {
        int output = 0;

        for (EnrichedFlight flight : ListOfEnrichedFlights){
            output += flight.getTimeInAir();
        }

        if(!ListOfEnrichedFlights.isEmpty()) {
            output = output / ListOfEnrichedFlights.size();
        }
        return output;
    }

    /**
     * This functions gets all flights stored in list of list and returns average time in the air for each list
     * @param listOfLists list of flights sorted by some parameter
     * @return average time in the air for each list in list of list stored in output format.
     */
    public List<Output> giveAllAverages(List<List<EnrichedFlight>> listOfLists){
        List<Output> output = new ArrayList<>();

        for(List<EnrichedFlight>  list : listOfLists) {
            if (!list.isEmpty()) {
                output.add(new Output(list.getFirst().getIcao24(), calculateAverageTimeInAir(list)));
            } else {
                output.add(new Output("EMPTY", calculateAverageTimeInAir(list)));
            }
        }

        return output;

    }

    /**
     * this function gives percentage of flights that classify as long per each list in list of lists
     * @param listOfLists
     * @return percentage of flight that classify as long stored in output format
     */
    public List<Output> givePercentageOfLongFlights (List<List<EnrichedFlight>> listOfLists){
        List<Output> output = new ArrayList<>();

        int processed = 0;
        int lastSent = 0;
        int total = listOfLists.size();
        for(List<EnrichedFlight>  list : listOfLists) {
            processed++;
            float counter = 0F;

            //Count amount of flight that classify as long
            for (EnrichedFlight flight : list) {

                if (flight.getTimeInAir() >= minimalTimeInAirForLongFlightSeconds) {
                    counter++;
                }
            }

            if (processed % NUMBER_OF_ROWS_TO_SEND_PROGRESS == 0 || processed == total) {
                int delta = processed - lastSent;
                UdpServer.sendProgress(delta, total);
                lastSent = processed;
            }

            //turn amount of flights that classify as long into percentage value
            if (counter != 0) {
                
                counter = counter / list.size();
                counter = counter *100;
            }
            //write output in output format
            if (!list.isEmpty()) {
                output.add(new Output(list.getFirst().getIcao24(), (int) counter));
            } else {
                output.add(new Output("EMPTY", (int) counter));
            }
        }
        int remaining = total - lastSent;
        if (remaining > 0) {
            UdpServer.sendProgress(remaining, total);
        }

        return output;
    }
}
