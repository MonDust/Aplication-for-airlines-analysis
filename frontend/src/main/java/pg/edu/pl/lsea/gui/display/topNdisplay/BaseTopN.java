package pg.edu.pl.lsea.gui.display.topNdisplay;

import pg.edu.pl.lsea.gui.display.BaseAnalysisDisplay;

import javax.swing.*;
import java.awt.*;

/**
 * Base class for top N displays - showing top N analysis.
 */
public abstract class BaseTopN extends BaseAnalysisDisplay {
    protected final StringBuilder messageLog = new StringBuilder();
    private final JTextArea logArea;

    /**
     * Class constructor - making logArea, the one to show the top N information.
     */
    public BaseTopN() {
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Returns the message log.
     * @return log
     */
    public String getLog() {
        return messageLog.toString();
    }

    /**
     * Shows the message log in a dialog.
     */
    public void showResultsDialog() {
        JOptionPane.showMessageDialog(null, getLog(), "Analysis Report", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Append a message to the log and update display.
     * @param msg message to log
     */
    protected void log(String msg) {
        messageLog.append(msg).append("\n");
        logArea.append(msg + "\n");
        // Auto-scroll
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
