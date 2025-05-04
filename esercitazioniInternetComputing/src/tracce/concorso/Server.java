package tracce.concorso;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    ServerSocket serverSocket;
    private final int TCP_PORT = 3000;
    private List<Concorso> concorsi;
    private static int counter = 0;
    private Map<Integer, Partecipazione> partecipazioni;

    private final int UDP_PORT = 4000;
    private DatagramSocket datagramSocket;
    private byte[] buf = new byte[256];

    private final int MULTICAST_PORT = 5000;
    private MulticastSocket multicastSocket;
    private byte[] buf2 = new byte[512];

    public Server() {
        try{
            serverSocket = new ServerSocket(TCP_PORT);
            concorsi = new ArrayList<>();
            partecipazioni = new ConcurrentHashMap<>();
            datagramSocket = new DatagramSocket(UDP_PORT);
            multicastSocket = new MulticastSocket(MULTICAST_PORT);
            InetAddress address = InetAddress.getByName("230.0.0.1");
            multicastSocket.joinGroup(address);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void verificaPartecipazione(){
        try{
            Socket s = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            Partecipazione part = (Partecipazione) ois.readObject();
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            if(verify(part)){
                for(Concorso c : concorsi){
                    if(c.getData().after(Calendar.getInstance().getTime()) && part.getID().equals(c.getID())){
                        StringBuilder sb = new StringBuilder();
                        sb.append(Calendar.getInstance().getTime()).append(" ").append(counter);
                        partecipazioni.put(counter, part);
                        counter++;
                        out.println(sb.toString());
                    }
                }
            }else{
                out.println("NOT_ACCEPTED");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    private boolean verify(Partecipazione part){
        return part.getCf() != null && part.getCognome() != null && part.getCurriculum() != null
                && part.getNome() != null && part.getID() != null;
    }

    public void eliminaPartecipazione(){
        try{
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            datagramSocket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            int ID = Integer.parseInt(received);
            String output = "false";
            if(partecipazioni.get(ID) != null){
                partecipazioni.remove(ID);
                output = "true";
            }
            buf = output.getBytes();
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            datagramSocket.send(new DatagramPacket(buf, buf.length, address, port));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void controllaVincitori(){
        try{
            List<String> cfVincitori = new ArrayList<>();
            String IDConcorso = "";
            for(Concorso c : concorsi){
                if(c.getData().before(Calendar.getInstance().getTime())){
                    IDConcorso = c.getID();
                    for(Partecipazione p : partecipazioni.values()){
                        if(p.getID().equals(IDConcorso)){
                            Random rand = new Random();
                            if(rand.nextBoolean()){
                                cfVincitori.add(p.getCf());
                            }
                        }
                    }
                }
            }
            String message = IDConcorso + " ";
            StringBuilder sb = new StringBuilder();
            for(String cf : cfVincitori){
                sb.append(cf).append(" ");
            }
            buf2 = sb.toString().getBytes();
            DatagramPacket packet = new DatagramPacket(buf2, buf2.length, datagramSocket.getInetAddress(), MULTICAST_PORT);
            multicastSocket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();

        new Thread(() -> {
            while(true){
                server.verificaPartecipazione();
            }
        }).start();

        new Thread(() -> {
            while(true){
                try{
                    server.controllaVincitori();
                    Thread.sleep(1000*60*60);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while(true){
                server.eliminaPartecipazione();
            }
        }).start();
    }
}
