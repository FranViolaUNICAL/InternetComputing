package tracce.sensoriAgricoli;
import java.time.LocalTime;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;


public class ServerSensore {
    private final int PORT = 3000;
    private Map<Socket, StatoSensore> sensori = new HashMap<>();
    private ServerSocket serverSocket;
    private Semaphore semaphore = new Semaphore(1);

    public ServerSensore() {
        try{
            serverSocket = new ServerSocket(PORT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addSocket(Socket s, StatoSensore st){
        sensori.put(s, st);
    }

    public void start(){
        while(true){
            try(Socket s = serverSocket.accept()){
                new SensorManager(s,this).start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ServerSensore().start();
    }
}

class SensorManager extends Thread {
    private Socket socket;
    private int countHum = 0;
    private int countTemp = 0;
    private int sumTemp = 0;
    private int sumHum = 0;
    private ServerSensore mainThread;

    public SensorManager(Socket socket, ServerSensore mainThread) {
        this.socket = socket;
        this.mainThread = mainThread;
    }

    public void run() {
        try{
            LocalTime oraAttuale = LocalTime.now();
            LocalTime inizio = LocalTime.of(8,0);
            LocalTime fine = LocalTime.of(13,0);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            boolean isActive = !oraAttuale.isBefore(inizio) && oraAttuale.isBefore(fine);
            while(true){
                String line = in.readLine();
                if(!isActive){
                    out.println("INACTIVE");
                }else{
                    StringTokenizer tk = new StringTokenizer(line);

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
