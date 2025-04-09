package pg.edu.pl.lsea.gui.analysis.displays.datadisplays;

import pg.edu.pl.lsea.data.analyzer.PropertiesCalculator;
import pg.edu.pl.lsea.data.analyzer.SortingCalculator;
import pg.edu.pl.lsea.data.analyzer.multithreading.ParallelGroupingTool;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.EnrichedFlight;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.analysis.displays.AnalysisDisplay;
import pg.edu.pl.lsea.gui.analysis.utils.AircraftParser;
import pg.edu.pl.lsea.gui.analysis.utils.DataStorageAnalysis;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static pg.edu.pl.lsea.utils.AnalysisTypeConstants.NUMBER_OF_MOST_POPULAR_OPERATORS;
import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.NUMBER_OF_DEFAULT_THREADS;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Class displaying plots for average times.
 */
public class PlotAverageTimePerOperatorDisplay extends AnalysisDisplay {
    private final AircraftParser parser;

    public PlotAverageTimePerOperatorDisplay(AircraftParser parser) {
        this.parser = parser;
    }

    /**
     * Get enriched flights grouped by the operator, then processed for Top N Operators.
     * @return List of lists of enriched flights
     */
    private List<List<EnrichedFlight>> getGroupedEnrichedFlights(SortingCalculator calc) {
        List<List<EnrichedFlight>> listOfLists;
        List<EnrichedFlight> flights = DataStorageAnalysis.prepareFlights();
        List<Aircraft> aircrafts = DataStorageAnalysis.prepareAircrafts();

        ParallelGroupingTool tool = new ParallelGroupingTool();
        List<List<EnrichedFlight>> listFlights = tool.groupFlightsByOperator(flights, aircrafts, NUMBER_OF_DEFAULT_THREADS);
        listOfLists = calc.giveTopNOperators(listFlights, NUMBER_OF_MOST_POPULAR_OPERATORS);

        return listOfLists;
    }

    /**
     * Function to plot the average time per operator using JFreeChart.
     * @param sortCalc - sorting calculator, making analysis possible
     */
    public void plotAverageTime(SortingCalculator sortCalc) {
        // Get the list of averages for the operators
        PropertiesCalculator calc = new PropertiesCalculator();
        List<Output> averages = calc.printAllAverages(getGroupedEnrichedFlights(sortCalc));

        DefaultCategoryDataset dataset = createDataset(averages);

        JFreeChart chart = createChart(dataset);

        JPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JOptionPane.showMessageDialog(null, chartPanel, "Average Time per Operator", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates a dataset for the bar chart.
     * @param averages - list of averages for each operator
     * @return the dataset to be used in the chart
     */
    private DefaultCategoryDataset createDataset(List<Output> averages) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Output output : averages) {
            String operator = parser.getAircraftByIcao(output.getIcao24()).getOperator();
            double value = output.Value;

            // Add the value to the dataset (category = operator, series = "Average Time")
            dataset.addValue(value, "Average Time", operator);
        }

        return dataset;
    }

    /**
     * Creates a bar chart using the provided dataset.
     * @param dataset - the dataset to be used in the chart
     * @return the created bar chart
     */
    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        // Create the chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Average Time per Operator",
                "Operator",
                "Average Time",
                dataset
        );
        chart.setBackgroundPaint(Color.white);

        return chart;
    }
}

