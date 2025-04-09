package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Output;

import java.util.ArrayList;
import java.util.List;

public class PropertiesCalculator {

    private static final int minimalTimeInAirForLongFlightSeconds = 1800;

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


    public int calculateAverageTimeInAir(List<EnrichedFlight> input) {

        int output = 0;

        for (EnrichedFlight flight : input){
            output += flight.getTimeInAir();
        }

        if(!input.isEmpty()) {
            output = output / input.size();
        }
        return output;
    }

    public List<Output> printAllAverages( List<List<EnrichedFlight>> listOfLists){
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



    public List<Output> givePercentageOfLongFlights (List<List<EnrichedFlight>> listOfLists){
        List<Output> output = new ArrayList<>();

        for(List<EnrichedFlight>  list : listOfLists) {
            float counter = 0F;
            
            for (EnrichedFlight flight : list) {
                if (flight.getTimeInAir() >= minimalTimeInAirForLongFlightSeconds) {
                    counter++;
                }
            }

            if (counter != 0) {
                
                counter = counter / list.size();
                counter = counter * 100;
            }
            if (!list.isEmpty()) {
                output.add(new Output(list.getFirst().getIcao24(), (int) counter));
            } else {
                output.add(new Output("EMPTY", (int) counter));
            }
        }

        return output;
    }
}
