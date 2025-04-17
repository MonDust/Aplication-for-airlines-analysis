package pg.edu.pl.lsea.gui.analysis.displays.results;

import javax.swing.*;

/**
 * Abstract class for displaying analysis results.
 */
public abstract class BaseRunner {

    // message log that will be displayed at the end
    protected final StringBuilder messageLog = new StringBuilder();

    /**
     * Append a message to the log and optionally display it.
     * @param msg message to log
     */
    protected void log(String msg) {
        messageLog.append(msg).append("\n");
    }

    /**
     * Shows the message log in a dialog.
     */
    public void showResultsDialog() {
        JOptionPane.showMessageDialog(null, getMessages(), "Analysis Report", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Returns the message log.
     * @return log
     */
    public String getMessages() {
        return messageLog.toString();
    }
}
