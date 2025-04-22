package pg.edu.pl.lsea.backend.udp;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class UdpServer {

    private static final int CLIENT_PORT = 51556;
    private static final int SERVER_PORT = 51555;
    private static DatagramSocket socket;
    private static InetAddress clientAddress;
    private static boolean running = true;


    static {
        try {
            socket = new DatagramSocket(SERVER_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public static void sendProgress(int rowsProcessed, int totalRows) {
        if (clientAddress == null) {
            System.err.println("Unknown client address, cannot send data");
            return;
        }
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(rowsProcessed);
            buffer.putInt(totalRows);
            byte[] data = buffer.array();

            DatagramPacket packet = new DatagramPacket(
                    data, data.length,
                    clientAddress, CLIENT_PORT
            );
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("UDP error: " + e.getMessage());
        }
    }

    public static void receiveClientAddress() {
        new Thread(() -> {
            try {
                byte[] buffer = new byte[8];

                while (running) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    clientAddress = packet.getAddress();
                    if (clientAddress != null) {
                        running=false;
                    }

                    System.out.println("Data received: " + clientAddress);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void closeServer() {
        socket.close();
        System.out.println("Server socket closed.");
    }
}
