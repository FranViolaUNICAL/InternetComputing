package datagrams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TimeClientTimeZone {
    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        String timeZone = "Europe/Berlin";
        DatagramSocket socket = new DatagramSocket();
        //invia la richiesta
        byte[] buf = new byte[1024];
        buf = timeZone.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 3575);
        socket.send(packet);
        //riceve la risposta
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        //visualizza la risposta
        String received = new String(packet.getData());
        System.out.println(received);
        socket.close();
    }
}
