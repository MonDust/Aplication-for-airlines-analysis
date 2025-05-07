package pg.edu.pl.lsea;

import pg.edu.pl.lsea.api.DataUploader;
import pg.edu.pl.lsea.entities.Aircraft;
import pg.edu.pl.lsea.entities.Flight;
import pg.edu.pl.lsea.files.CsvDataReader;
import pg.edu.pl.lsea.gui.GuiInitializer;
import pg.edu.pl.lsea.udp.UdpClient;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        try {
            // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (Exception e) {
            e.printStackTrace();
        }

        UdpClient.startListening();

        GuiInitializer.initialize();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UdpClient.stopListening();
        }));
    }
}
