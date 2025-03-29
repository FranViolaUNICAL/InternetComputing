package datagrams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeServerTimeZone {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(3575);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String timeZone = new String(packet.getData());
            Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
            System.out.println("Received request from " + packet.getAddress() + ":" + packet.getPort() + " for timeZone " + timeZone);
            String response = myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE);
            buf = response.getBytes();
            packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
            socket.send(packet);
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
            socket.close();
        }
    }
}
