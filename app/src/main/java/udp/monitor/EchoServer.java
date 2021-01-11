package udp.monitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {
    private DatagramSocket socket;
    private boolean running;

    /*
    * Opens a new socket on the specified port and listens for incoming udp packets.
    * Any packets received are immediately sent back to the sender.
    * Packet buffer is capped at 256 bytes.
    * Shuts down the socket and stops when packet received contains "end".
    */
    public EchoServer(int port) {
        try {
            socket = new DatagramSocket(port);
            System.out.println(String.format("[Server] Server started with address/port %s:%s", socket.getLocalAddress().toString(), socket.getPort()));
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;
        byte[] buf;

        while (running) {
            buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(String.format("[Server] Client %s:%s sent: %s", address, port, received));

            try {
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (received.equals("end")) {
                System.out.println("[Server] End message received, shutting down");
                socket.close();
                running = false;
            }
        }
    }
}