

package pg.edu.pl.lsea.gui.udp;

import pg.edu.pl.lsea.udp.UdpClient;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class displaying progress bar - the status of processing.
 */
public class ProgressBarUDP extends JFrame {
    private final JProgressBar progressBar;
    private final JLabel progressLabel;
    private final Timer timer = new Timer();
    private boolean firstUpdate = true;

    /**
     * Constructor for the class.
     */
    public ProgressBarUDP() {
        setTitle("Analysis Progress");
        setLayout(new BorderLayout());
        setSize(400, 120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Label
        progressLabel = new JLabel("Analysis in progress...", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(progressLabel, BorderLayout.NORTH);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.CENTER);

        // Start listening
        // UdpClient.startListening();

        int percent = 0;
        progressBar.setValue(percent);
        progressLabel.setText("Progress: " + percent + "%");

        // Timer to update progress
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int total = UdpClient.getTotalProcessed();
                int sum = UdpClient.getSumProcessed();

                if (total > 0) {
                    int percent = (int) ((sum / (double) total) * 100);

                    // Update the progress bar and label
                    progressBar.setValue(percent);
                    progressLabel.setText("Progress: " + percent + "%");
                    // progressBar.repaint();
                    // progressLabel.repaint();

                    System.out.println("Progress: " + percent + "%");

                    if (sum >= total) {
                        progressLabel.setText("Analysis completed!");
                        timer.cancel();
                        // UdpClient.stopListening();

                        UdpClient.setSumProcessed(0);
                        UdpClient.setTotalProcessed(0);

                        // Auto-close after 2 seconds
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                dispose();
                            }
                        }, 2000);
                    }
                }
            }
        }, 0, 10);// Update

        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                UdpClient.setSumProcessed(0);
                UdpClient.setTotalProcessed(0);
            }
        });
    }
}