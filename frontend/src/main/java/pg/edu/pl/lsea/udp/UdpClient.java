package pg.edu.pl.lsea.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UdpClient {

    private static boolean listening = false;
    private static Thread listenerThread;
    private static volatile int sumProcessed = 0;
    private static volatile int totalProcessed = 0;
    private static final String serverIP = "localhost";
    private static final int SERVER_PORT = 51555;
    private static final int CLIENT_PORT = 51556;


    public static int getSumProcessed() {
        return sumProcessed;
    }

    public static int getTotalProcessed() {
        return totalProcessed;
    }



    public static void startListening() {
        if (listening) {
            System.out.println("Listening");
            return;
        }

        listening = true;
        listenerThread = new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket(CLIENT_PORT);
                byte[] buffer = new byte[8];

                System.out.println("Listening to port: " + CLIENT_PORT);
                requestDataTransfer();
                while (listening) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    ByteBuffer byteBuffer = ByteBuffer.wrap(packet.getData());
                    int processed = byteBuffer.getInt();
                    int total = byteBuffer.getInt();

                    sumProcessed = sumProcessed + processed;
                    totalProcessed = total;
                }

                socket.close();

            } catch (Exception e) {
                if (listening) e.printStackTrace();
            }
        });

        listenerThread.start();
    }

    public static void stopListening() {
        listening = false;
        System.out.println("Stopped listening");
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    public static void requestDataTransfer() {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[8];

            buffer[0] = 1;

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverIP), SERVER_PORT);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
