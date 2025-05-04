package tracce.concorso;

import javax.xml.crypto.Data;
import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
    final int TCP_PORT = 3000;
    private InetAddress serverAddress;
    private Socket socket;

    final int UDP_PORT = 4000;
    private DatagramSocket datagramSocket;
    private byte[] buf = new byte[256];

    final int MULTICAST_PORT = 5000;
    private MulticastSocket multicastSocket;
    private byte[] buf2 = new byte[512];

    private Map<Integer, Partecipazione> domandeInviate = new HashMap<>();

    public Client() throws IOException {
        serverAddress = InetAddress.getLocalHost();
        datagramSocket = new DatagramSocket(UDP_PORT, serverAddress);
        multicastSocket = new MulticastSocket(MULTICAST_PORT);
        socket = new Socket(serverAddress.getHostAddress(), TCP_PORT);
        multicastSocket.joinGroup(InetAddress.getByName("230.0.0.1"));
    }

    public void mandaPartecipazione(String nome, String cognome, String cf, String ID, String curriculum){
        Partecipazione part = new Partecipazione(ID, nome, cognome, cf, curriculum);
        ObjectOutputStream objectOutputStream = null;
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(part);
            String response = bufferedReader.readLine();
            StringTokenizer st = new StringTokenizer(response);
            response = st.nextToken();
            if(response.equalsIgnoreCase("NOT_ACCEPTED")){
                System.out.println("REQUEST NOT ACCEPTED");
            }else{
                int IDProtocollo = Integer.parseInt(response);
                domandeInviate.put(IDProtocollo, part);
                System.out.println("REQUEST ACCEPTED ON: " + st.nextToken());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cancellaPartecipazione(String ID){
        Partecipazione part = domandeInviate.get(ID);
        if(part != null){
            try{
                buf = ID.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, UDP_PORT);
                datagramSocket.send(packet);
                byte[] buf3 = new byte[256];
                packet = new DatagramPacket(buf3, buf3.length);
                datagramSocket.receive(packet);
                String response = new String(packet.getData());
                boolean result = Boolean.parseBoolean(response);
                if(result){
                    System.out.println("PARTECIPAZIONE CANCELLATA");
                }else{
                    System.out.println("PARTECIPAZIONE NON CANCELLATA");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void riceviVincitori(){
        try{
            DatagramPacket packet = new DatagramPacket(buf2, buf2.length);
            multicastSocket.receive(packet);
            String vincitori = new String(packet.getData());
            if(!vincitori.equals("")){
                System.out.println(vincitori);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        new Thread(() -> {
            while(true){
                client.riceviVincitori();
            }
        }).start();
    }
}
