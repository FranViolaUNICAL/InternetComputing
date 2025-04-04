package communicator;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.*;
import java.net.*;
import java.io.*;

public class MulticastHandler extends Thread {
    private static final String MULTICAST_ADDRESS = "230.0.0.1";
    private static final int MULTICAST_PORT = 2000;
    private HashMap<String, Integer> discoveredUsers = new HashMap<>();
    private String myAddress;
    private int myPort;

    public MulticastHandler(int port) {
        try{
            this.myAddress = InetAddress.getLocalHost().getHostAddress();
            this.myPort = port;
        }catch (UnknownHostException uhe){
            System.out.println(uhe);
        }
    }

    public void run() {
        try{
            MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            new Thread(() -> {
                try{
                    DatagramSocket sendSocket = new DatagramSocket();
                    byte[] buf = new byte[1024];
                    String message = myAddress + " " + myPort;
                    buf = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);
                    while(true){
                        sendSocket.send(packet);
                        Thread.sleep(5000);
                    }
                }catch (Exception e){
                    System.out.println(e);
                }
            }).start();
            byte[] buf = new byte[1024];
            while(true){
                DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
                socket.receive(receivePacket);
                String received = new String(receivePacket.getData());
                StringTokenizer tokenizer = new StringTokenizer(received, " ");
                String addressReceived = tokenizer.nextToken();
                int portReceived = Integer.parseInt(tokenizer.nextToken());
                //if(!addressReceived.equals(myAddress)){
                    discoveredUsers.put(addressReceived, portReceived);
                //}
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getDiscoveredUsers() {
        return discoveredUsers;
    }
}
