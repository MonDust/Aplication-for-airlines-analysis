package pg.edu.pl.lsea.gui.display.datadisplay;

import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.display.BaseAnalysisDisplay;
import pg.edu.pl.lsea.gui.display.datadisplay.tables.OutputTableDisplayWithAircraft;
import pg.edu.pl.lsea.gui.display.datadisplay.tables.OutputTableDisplayWithFlight;

import java.util.ArrayList;
import java.util.List;

public class Groupedanalysis extends BaseAnalysisDisplay {

    /**
     * Constructor for the class.
     */
    public Groupedanalysis() {
    }

    /**
     * Analyse grouped by the model - show a display table with flights grouped by model.
     */
    public void analyseGroupedByModel() {
        // TODO - get output from analysis: API
        // get the list (analysis)
        List<Output> outputList = new ArrayList();
        new OutputTableDisplayWithFlight(outputList);
    }

    /**
     * Analyse grouped by the operator - show a display table with flights grouped by model.
     */
    public void analyseGroupedByOperator() {
        // TODO - get output from analysis: API
        // get the list (analysis)
        List<Output> outputList = new ArrayList();
        new OutputTableDisplayWithAircraft(outputList);

    }

}
