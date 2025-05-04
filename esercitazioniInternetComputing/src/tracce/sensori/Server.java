package tracce.sensori;

import java.util.*;
import java.io.*;
import java.net.*;

public class Server {
    Map<Integer, Misura> misure = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        new ClientHandler(server).start();
        new SensorHandler(server).start();
        new InactivityHandler(server).start();
    }
}

class ClientHandler extends Thread {
    private ServerSocket serverSocket;
    private Server server;

    public ClientHandler(Server server) throws IOException {
        serverSocket = new ServerSocket(3000);
        this.server = server;
    }

    public void run() {
        try{
            while (true) {
                Socket s = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String line = in.readLine();
                int id = Integer.parseInt(line);
                Misura m = server.misure.get(id);
                if(m != null) {
                    out.writeObject(m);
                    out.flush();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

class SensorHandler extends Thread {
    //Handler sensori in UDP
    private DatagramSocket socket;
    private byte[] buf = new byte[4096];
    Server server;


    public SensorHandler(Server server) throws IOException, ClassNotFoundException {
        socket = new DatagramSocket(4000);
        this.server = server;
    }

    public void run() {
        try{
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Misura received = (Misura) ois.readObject();
                server.misure.put(received.getIdSensore(), received);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class InactivityHandler extends Thread {
    private MulticastSocket socket;
    private byte[] buf = new byte[256];
    private Server server;

    public InactivityHandler(Server server) throws IOException {
        socket = new MulticastSocket(5000);
        InetAddress group = InetAddress.getByName("230.0.0.1");
        socket.joinGroup(group);
        this.server = server;
    }

    public void run() {
        while (true) {
            StringBuilder sb = new StringBuilder();
            long currentTime = System.currentTimeMillis();
            for(Integer ID : server.misure.keySet()) {
                Misura misura = server.misure.get(ID);
                if(currentTime - misura.getTimestamp() >= 1000 * 60 * 10){
                    if(sb.length()<255){
                        sb.append(ID + " ");
                    }
                }
            }
            buf = sb.toString().getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000*60*10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
