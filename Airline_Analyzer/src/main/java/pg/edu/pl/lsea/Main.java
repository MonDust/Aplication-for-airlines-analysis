package pg.edu.pl.lsea;

import pg.edu.pl.lsea.gui.MainFrame;
import pg.edu.pl.lsea.gui.MainPanel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        MainPanel mainPanel = new MainPanel();
//         TODO - uncomment after implementing AnalysisPanel
//        AnalysisPanel analysisPanel = new AnalysisPanel();
//        mainPanel.add(analysisPanel);
        MainFrame mainFrame = new MainFrame(mainPanel);
    }
}
