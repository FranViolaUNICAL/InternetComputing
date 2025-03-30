package multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class MulticastTimeServer {
    public static void main(String[] args) {
        MulticastSocket socket = null;
        try{
            socket = new MulticastSocket(3575);
            while(true){
                byte[] buf = new byte[256];
                // non aspetta la richiesta
                String dString = new Date().toString();
                buf = dString.getBytes();
                // invia messaggio in broadcast
                InetAddress address = InetAddress.getByName("230.0.0.1");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 3575);
                socket.send(packet);
                System.out.println(dString);
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
            socket.close();
        }
    }
}
