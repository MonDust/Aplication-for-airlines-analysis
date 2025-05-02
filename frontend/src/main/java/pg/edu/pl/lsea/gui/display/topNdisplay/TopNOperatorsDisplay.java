package pg.edu.pl.lsea.gui.display.topNdisplay;

import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Output;

import java.util.ArrayList;
import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.NUMBER_OF_MOST_POPULAR_OPERATORS;

/**
 * Class responsible for returning Top N Operators - showing a windows with an appropriate message.
 */
public class TopNOperatorsDisplay extends BaseTopN {

    /**
     * Constructor for the class.
     */
    public TopNOperatorsDisplay() {
        displayTopOperators();
    }

    /**
     * Method to perform the Top N operators analysis and display the results.
     * n established by constant.
     */
    public void displayTopOperators() {
        // TODO - Make it receive results of corresponding analysis and display them accordingly

        List<Output> sizes = dataLoader.giveTopNOperators(NUMBER_OF_MOST_POPULAR_OPERATORS);
                //new ArrayList();

        //SIZE (number of flights) - value
        //Output o = new Output("PlaceholderIcao", 0);
        //sizes.add(o);

        log("Top " + sizes.size() + " Operators:");
        for (int i = 0; i < sizes.size(); i++) {
            // get it with Aircraft
            String Operator = dataLoader.getAircraftIcao(sizes.get(i).getIcao24()).getOperator();
                    //"PlaceholderOperator";

            log("Operator " + Operator + ": " + sizes.get(i).getValue() + " flights");
        }
    }
}
