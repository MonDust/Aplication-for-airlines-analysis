//package pg.edu.pl.lsea.gui;
//
//import pg.edu.pl.lsea.udp.UdpClient;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class ProgressBarUDP extends JFrame {
//    private final JProgressBar progressBar;
//    private final JLabel progressLabel;
//    private final Timer timer = new Timer();
//
//    public ProgressBarUDP() {
//        setTitle("Analysis Progress");
//        setLayout(new BorderLayout());
//        setSize(400, 120);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        // Label
//        progressLabel = new JLabel("Analysis in progress...", SwingConstants.CENTER);
//        progressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
//        add(progressLabel, BorderLayout.NORTH);
//
//        // Progress bar
//        progressBar = new JProgressBar(0, 100);
//        progressBar.setStringPainted(true);
//        add(progressBar, BorderLayout.CENTER);
//
//        // Start listening
//        UdpClient.startListening();
//
//        // Timer to update progress
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                int total = UdpClient.getTotalProcessed();
//                int sum = UdpClient.getSumProcessed();
//
//                if (total > 0) {
//                    int percent = (int) ((sum / (double) total) * 100);
//                    progressBar.setValue(percent);
//                    progressLabel.setText("Progress: " + percent + "%");
//
//                    if (sum >= total) {
//                        progressLabel.setText("Analysis completed!");
//                        timer.cancel();
//                        UdpClient.stopListening();
//
//                        // Optional: Auto-close after 2 seconds
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                dispose();
//                            }
//                        }, 2000);
//                    }
//                }
//            }
//        }, 0, 500); // Update every 0.5 sec
//
//        setVisible(true);
//    }
//}
