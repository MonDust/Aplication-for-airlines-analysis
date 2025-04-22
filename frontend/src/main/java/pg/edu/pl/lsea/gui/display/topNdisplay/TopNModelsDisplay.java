package pg.edu.pl.lsea.gui.display.topNdisplay;

import pg.edu.pl.lsea.entities.Output;

import java.util.List;

/**
 * Class responsible for returning Top N Models - showing a windows with an appropriate message.
 */
public class TopNModelsDisplay extends BaseTopN {

    public TopNModelsDisplay() {
        displayTopModels();
    }

    /**
     * Method to perform the Top N models analysis and display the results.
     * n established by constant.
     */
    public void displayTopModels() {
        // TODO - Make it receive results of corresponding analysis and display them accordingly

        List<Output> sizes = List.of();

        //SIZE - value
        Output o = new Output("PlaceholderIcao", 0);
        sizes.add(o);

        log("Top " + sizes.size() + " Models:");
        for (int i = 0; i < sizes.size(); i++) {
            // get it with Aircraft and Output
            String Model = "PlaceholderModel";

            log("Model " + Model + ": " + sizes.get(i).getValue() + " flights");
        }
    }
}
