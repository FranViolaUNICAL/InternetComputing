package tracce.asta;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ServerAsta {
    public static void main(String[] args) {
        Asta asta1 = new Asta("iPhone8");
        Asta asta2 = new Asta("iPhone9");
        Asta asta3 = new Asta("iPhone10");
        List<Asta> aste = new ArrayList<>();
        aste.add(asta1);
        aste.add(asta2);
        aste.add(asta3);
        for(Asta asta : aste) {
            new MulticastHandler(asta).start();
        }
    }
}

class MulticastHandler extends Thread{
    private final int MULTICAST_PORT = 5000;
    private final String MULTICAST_ADDRESS = "230.0.0.1";
    public MulticastSocket multicastSocket;
    private Asta asta;

    public MulticastHandler(Asta asta){
        this.asta = asta;
        try{
            multicastSocket = new MulticastSocket(MULTICAST_PORT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
            byte[] buffer = new byte[200];
            int tcp_port = new Random().nextInt(0, 10000) + 30000;
            String message = asta.toString() + " " + tcp_port;
            buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);
            int counter = 0;
            new TCPHandler(tcp_port).start();
            while(counter < 60){
                multicastSocket.send(packet);
                System.out.println("Sent packet containing: " + message);
                Thread.sleep(1000 * 60);
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class TCPHandler extends Thread{
    private int port;
    private ServerSocket serverSocket;
    private TreeMap<Integer, Socket> clients = new TreeMap<>();
    private Semaphore semaphore = new Semaphore(1);
    private final long TIMEOUT = 60 * 60 * 1000;
    private final long CURRENT_TIME = System.currentTimeMillis();
    private boolean winnerSelected = false;

    public TCPHandler(int port){
        try{
            this.port = port;
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000 * 60 * 60);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            while(true){
                while(System.currentTimeMillis() - CURRENT_TIME < TIMEOUT){
                    Socket client = serverSocket.accept();
                    new ConnectionHandler(client, this).start();
                }
                if(!winnerSelected){
                    checkOffer();
                }else{
                    serverSocket.close();
                }
                Socket client = serverSocket.accept();
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println("Asta conclusa.");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void addClient(Socket client, int amount){
        try{
            semaphore.acquire();
            clients.put(amount, client);
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            semaphore.release();
        }
    }

    public boolean isOver(){
        return winnerSelected;
    }

    public void checkOffer(){
        try{
            semaphore.acquire();
            Socket winner = clients.lastEntry().getValue();
            winnerSelected = true;
            InetAddress address = winner.getInetAddress();
            Socket winnerSocket = new Socket(address, 4000);
            PrintWriter out = new PrintWriter(winnerSocket.getOutputStream(), true);
            out.println("Asta vinta!");
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            semaphore.release();
        }
    }
}

class ConnectionHandler extends Thread{
    private Socket client;
    private TCPHandler mainThread;

    public ConnectionHandler(Socket client, TCPHandler mainThread){
        this.client = client;
        this.mainThread = mainThread;
    }

    public void run(){
        while(true){
            try{
                System.out.println("Established connection with: " + client.getInetAddress());
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                String line = in.readLine();
                StringTokenizer st = new StringTokenizer(line);
                if(st.countTokens() == 3){
                    int amount = 0;
                    int counter = 0;
                    while(st.hasMoreTokens()){
                        if(counter != 2){
                            st.nextToken();
                            counter++;
                        }else{
                            amount = Integer.parseInt(st.nextToken());
                        }
                    }
                    mainThread.addClient(client, amount);
                    out.println("true");
                }
                else{
                    out.println("false");
                }
                if(mainThread.isOver()){
                    out.println("Asta finita.");
                    client.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
