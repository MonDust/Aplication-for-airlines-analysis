package pg.edu.pl.lsea.data.analyzer;

import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;

import java.util.List;

public class CorrelationCalculator {

    public float calculateCoreelation (float[] List1, float[] List2) {

        float output = 0;

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

}
