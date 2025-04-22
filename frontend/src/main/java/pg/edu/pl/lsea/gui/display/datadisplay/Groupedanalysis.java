package pg.edu.pl.lsea.gui.display.datadisplay;

import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.display.BaseAnalysisDisplay;
import pg.edu.pl.lsea.gui.display.datadisplay.tables.OutputTableDisplayWithAircraft;
import pg.edu.pl.lsea.gui.display.datadisplay.tables.OutputTableDisplayWithFlight;

import java.util.List;

public class Groupedanalysis extends BaseAnalysisDisplay {
    public Groupedanalysis() {
    }

    public void analyseGroupedByModel() {
        // get the list (analysis)
        List<Output> outputList = List.of();
        new OutputTableDisplayWithFlight(outputList);
    }

    public void analyseGroupedByOperator() {
        // get the list (analysis)
        List<Output> outputList = List.of();
        new OutputTableDisplayWithAircraft(outputList);

    }

}
