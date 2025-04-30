package pg.edu.pl.lsea.gui.display.topNdisplay;

import pg.edu.pl.lsea.entities.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for returning Top N Models - showing a windows with an appropriate message.
 */
public class TopNModelsDisplay extends BaseTopN {

    /**
     * Constructor fo the class.
     */
    public TopNModelsDisplay() {
        displayTopModels();
    }

    /**
     * Method to perform the Top N models analysis and display the results.
     * n established by constant.
     */
    public void displayTopModels() {
        // TODO - Make it receive results of corresponding analysis and display them accordingly

        System.out.println("here2");
        List<Output> sizes = dataLoader.giveTopNModels();
        //new ArrayList();

        //SIZE (number of flights) - value
        //Output o = new Output("PlaceholderIcao", 0);
        //sizes.add(o);

        log("Top " + sizes.size() + " Models:");
        System.out.println("Here 3");
        for (int i = 0; i < sizes.size(); i++) {
            // get it with Aircraft and Output
            String Model = dataLoader.getAircraftIcao(sizes.get(i).getIcao24()).getModel();
                    //"PlaceholderModel";

            log("Model " + Model + ": " + sizes.get(i).getValue() + " flights");
        }
    }
}
