package pg.edu.pl.lsea;

import pg.edu.pl.lsea.gui.AnalysisArea;
import pg.edu.pl.lsea.gui.MainFrame;
import pg.edu.pl.lsea.gui.MainPanel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        MainPanel mainPanel = new MainPanel();
        AnalysisArea analysisArea = new AnalysisArea();
        mainPanel.add(analysisArea);
        MainFrame mainFrame = new MainFrame(mainPanel);
    }
}
