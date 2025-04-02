package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.files.CsvDataLoader;
import pg.edu.pl.lsea.gui.MainPanel;
import pg.edu.pl.lsea.gui.analysis.AnalysisPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Base class for all available buttons
 */
public abstract class LoadDataButton extends JButton implements ActionListener {
    protected final MainPanel mainPanel;
    protected AnalysisPanel analysisPanel;
    protected final CsvDataLoader dataLoader;


    public LoadDataButton(MainPanel panel) {
        mainPanel = panel;
        dataLoader = new CsvDataLoader();
        setFont(new Font("Arial", Font.BOLD, 14));
        setFocusable(false);
        addActionListener(this);
    }

    /**
     * Setting the analysis panel, which will be influenced by loading the data by the button
     * @param panel
     */
    public void setAnalysisPanel(AnalysisPanel panel) {
        analysisPanel = panel;
    }


    /**
     * Choose file from the available
     * @return - chosen file to load the data
     */
    protected File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
