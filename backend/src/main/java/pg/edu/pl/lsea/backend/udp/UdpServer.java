package pg.edu.pl.lsea.backend.udp;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * A class for sending analysis progress information through udp to the client.
 */
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

    /**
     * Sends information about analysis progress to the client.
     * @param rowsProcessed amount of rows proccessed since last message
     * @param totalRows total amount of rows to be proccessed
     */
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

    /**
     * Starts a listener thread that waits for data request from a client or information about client disconnecting.
     * Reads the client address from the request.
     */
    public static void receiveClientAddress() {
        new Thread(() -> {
            try {

                while (running) {
                    byte[] buffer = new byte[8];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    byte code = buffer[0];
                    InetAddress address = packet.getAddress();
                    switch (code) {
                        case 1:
                            clientAddress = address;
                            System.out.println("Client connected from: " + clientAddress);
                            break;
                        case 2:
                            System.out.println("Client disconnected: " + address);
                            clientAddress = null;
                            break;
                        default:
                            System.out.println("Unknown code: " + code);
                    }
                }

            }   catch (IOException e) {
                if (!socket.isClosed()) {
                e.printStackTrace();
                } else {
                    System.out.println("Socket closed, exiting listener thread.");
                }
            }
        }).start();
    }

    /**
     * Closes the server socket.
     */
    public static void closeServer() {
        socket.close();
        System.out.println("Server socket closed.");
    }
}
