package pg.edu.pl.lsea.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class for receiving analysis progress information through udp from the server.
 */
public class UdpClient {

    private static boolean listening = false;
    private static Thread listenerThread;
    private static volatile AtomicInteger sumProcessed = new AtomicInteger(0);
    private static volatile AtomicInteger totalProcessed = new AtomicInteger(0);
    /**
     * !!! Change the value of serverIP when running server on different device.
     */
    private static final String serverIP = "localhost";
    private static final int SERVER_PORT = 51555;
    private static final int CLIENT_PORT = 51556;

    /**
     * Returns the amount of data processed during the analysis, from the server.
     * @return amount of proccessed rows of data
     */
    public static int getSumProcessed() {
        return sumProcessed.get();
    }

    public static void setSumProcessed(int sum) {
        sumProcessed.set(sum);
    }

    public static void setTotalProcessed(int total) {
        totalProcessed.set(total);
    }

    /**
     * Returns the total amount of data to be processed during the analysis, from the server.
     * @return total amount of rows of data to be processed
     */
    public static int getTotalProcessed() {
        return totalProcessed.get();
    }

    /**
     *  Starts a listener thread for receiving information about analysis progress.
     */
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

                    sumProcessed.addAndGet(processed);
                    totalProcessed.set(total);
                }

                socket.close();

            } catch (Exception e) {
                if (listening) e.printStackTrace();
            }
        });

        listenerThread.start();
    }

    /**
     * Stops the listener thread, no longer waiting for analysis progress data.
     */
    public static void stopListening() {
        listening = false;
        sendDisconnect();
        System.out.println("Stopped listening");
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    /**
     * Sends the request for analysis progress data. Allows the server to get the client address for sending data.
     */
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

    /**
     * Informs the server about client disconnecting.
     */
    private static void sendDisconnect() {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[8];
            buffer[0] = 2;
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverIP), SERVER_PORT);
            socket.send(packet);
            socket.close();
            System.out.println("Sent disconnect to server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
