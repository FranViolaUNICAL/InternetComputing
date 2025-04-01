package betserver;
import java.io.*;
import java.net.*;
import java.util.*;

public class BetClient {
    private int serverPort;
    private int myPort;
    private InetAddress groupAddress;
    private InetAddress serverAddress;
    private Socket s;

    public BetClient(InetAddress gAddress, InetAddress server, int sPort, int mPort) {
        groupAddress = gAddress;
        serverAddress = server;
        serverPort = sPort;
        myPort = mPort;
        try{
            s = new Socket(serverAddress, serverPort);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public boolean placeBet(int nCavallo, long puntata){
        String e = "";
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            String bet = nCavallo + " " + puntata;
            out.println(bet);
            e=in.readLine();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return e.equals("Scommessa Accettata");
    }

    public void riceviElencoVincitori(){
        try{
            MulticastSocket socket = new MulticastSocket(myPort);
            socket.joinGroup(groupAddress);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String elenco = new String(packet.getData());
            System.out.println("Elenco Vincitori: ");
            System.out.println(elenco);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args){
        int serverPort = 8001;
        int myPort = 8002;
        try{
            InetAddress group = InetAddress.getByName("230.0.0.1");
            InetAddress server = InetAddress.getByName("127.0.0.1");
            BetClient client = new BetClient(group, server, serverPort, myPort);
            int cavallo=((int)(Math.random()*12))+1;
            int cifra=((int)(Math.random()*100))+1;
            if(client.placeBet(cavallo,cifra)){
                client.riceviElencoVincitori();
            }
        }catch (UnknownHostException uhe){
            System.out.println(uhe);
        }
    }
}
