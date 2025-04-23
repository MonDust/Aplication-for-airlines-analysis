package pg.edu.pl.lsea.gui.display.graphdisplay;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import pg.edu.pl.lsea.entities.Output;
import pg.edu.pl.lsea.gui.display.BaseAnalysisDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class displaying plots for average times.
 */
public class PlotAverageTimePerOperatorDisplay extends BaseAnalysisDisplay {

    /**
     * Constructor for the class.
     */
    public PlotAverageTimePerOperatorDisplay() {
        // Get the list of averages for the operators
        // TODO - these averages should be taken from API and passed as an argument to 'plotAverageTime' function - we cannot be calculating any data on the client side
        List<Output> avereges = new ArrayList();
        plotAverageTime(avereges);

    }

    /**
     * Function to plot the average time per operator using JFreeChart.
     */
    public JPanel plotAverageTime(List<Output> averages) {
        DefaultCategoryDataset dataset = createDataset(averages);

        JFreeChart chart = createChart(dataset);

        JPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // DELETE BELOW //
        // JOptionPane.showMessageDialog(null, chartPanel, "Average Time per Operator", JOptionPane.INFORMATION_MESSAGE);

        return chartPanel;
    }

    /**
     * Creates a dataset for the bar chart.
     * @param averages - list of averages for each operator
     * @return the dataset to be used in the chart
     */
    private DefaultCategoryDataset createDataset(List<Output> averages) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Output output : averages) {
            // TODO Here operator names should be passed from API
            //  (by Icao), not joined manually as it was before
            String operator = "placeholder " + averages;

            double value = output.getValue();

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

