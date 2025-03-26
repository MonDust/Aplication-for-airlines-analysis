package pg.edu.pl.lsea.gui.buttons;

import pg.edu.pl.lsea.gui.MainPanel;

import java.awt.event.ActionEvent;

import static pg.edu.pl.lsea.utils.Constants.DisplayLayout.*;

public class AnalyseDataButton extends LoadDataButton {
    public AnalyseDataButton(MainPanel panel) {
        super(panel);

        setText("Analyse");
        setBounds(BUTTON_X + BUTTON_WIDTH * 2 + 20, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        analysisPanel.showAnalysis();
    }

}
