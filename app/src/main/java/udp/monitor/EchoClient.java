package udp.monitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress inetAddress;
    private int port;

    /*
     * Opens a new socket on the specified address and port. [Address]: The ip
     * address to which udp packets will be sent. If null, will use localhost.
     * [Port]: A specific port to use. If invalid, will use port 4445.
     */
    public EchoClient(String address, int port) {
        try {
            inetAddress = InetAddress.getByName((address != null) ? address : "localhost");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            socket = new DatagramSocket(port, inetAddress);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Opens a new socket on a wildcard port. [Address]: The ip address to which udp
     * packets will be sent. If null, will use localhost.
     */
    public EchoClient(String address) {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            inetAddress = InetAddress.getByName((address != null) ? address : "localhost");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Sends udp packets to the specified ip address. If an ip is not specified,
     * localhost is used. Once the packet is sent, immediately begins listening for
     * a response with the sent packet. Returns the packet contents once it is
     * received.
     */
    public String sendEcho(String msg, String serverAddress, String serverPort) {
        byte[] buf = msg.getBytes();
        String received = "";
        DatagramPacket packet;
        try {
            packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(serverAddress), Integer.parseInt(serverPort));
            System.out.println(String.format("[Client] Sending message to server at %s:%s", inetAddress.toString(), this.port));
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            received = new String(packet.getData(), 0, packet.getLength());
        } catch (NumberFormatException | IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return received;
    }

    public void close() {
        socket.close();
    }
}