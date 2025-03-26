package pg.edu.pl.lsea;

import pg.edu.pl.lsea.gui.analysis.AnalysisPanel;
import pg.edu.pl.lsea.gui.MainFrame;
import pg.edu.pl.lsea.gui.MainPanel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        MainPanel mainPanel = new MainPanel();
        AnalysisPanel analysisPanel = new AnalysisPanel();
        mainPanel.setAnalysisPanel(analysisPanel);

        MainFrame mainFrame = new MainFrame(mainPanel);
    }
}
