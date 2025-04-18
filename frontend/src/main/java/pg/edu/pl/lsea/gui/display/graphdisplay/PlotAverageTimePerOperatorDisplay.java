package pg.edu.pl.lsea.gui.display.graphdisplay;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.display.BaseAnalysisDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Class displaying plots for average times.
 */
public class PlotAverageTimePerOperatorDisplay extends BaseAnalysisDisplay {

    public PlotAverageTimePerOperatorDisplay() {}

    /**
     * Function to plot the average time per operator using JFreeChart.
     */
    public void plotAverageTime(List<Output> averages) {
        // Get the list of averages for the operators
//        List<Output> averages = calc.printAllAverages(getGroupedEnrichedFlights(sortCalc)); // REMOVE IT

        // TODO - these averages should be taken from API and passed as an argument to 'plotAverageTime' function - we cannot be calculating any data on the client side
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
            // TODO Here operator names should be passed from API, not joined manually as it was before
            String operator = "placeholder " + averages;
//            String operator = parser.getAircraftByIcao(output.getIcao24()).getOperator();
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

