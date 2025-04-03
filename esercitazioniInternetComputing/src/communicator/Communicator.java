package communicator;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Communicator {
    private int myPort;
    private String myAddress;
    public Communicator(int myPort, String myAddress) {
        this.myPort = myPort;
        this.myAddress = myAddress;
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        int port;
        String address = "localhost";
        Scanner in = new Scanner(System.in);
        System.out.println("Inserisci porta: ");
        port = in.nextInt();
        in.nextLine();
        Communicator communicator = new Communicator(port, address);
        MulticastHandler multicastHandler = new MulticastHandler(port);
        multicastHandler.start();
        AtomicReference<HashMap<String, Integer>> discUsers = new AtomicReference<>(new HashMap<>());
        new Thread(() -> {
            while(true){
                discUsers.set(multicastHandler.getDiscoveredUsers());
            }
        }).start();
        while(discUsers.get().isEmpty()){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                break;
            }
        }
        while(true){
            System.out.println("Please choose a user, or type USERS to see all discovered users.");
            String choosenAddress = in.nextLine();
            if(choosenAddress.equals("USERS")){
                System.out.println(discUsers.get());
            }else{
                System.out.println("Please input the port number: ");
                int portNumber = in.nextInt();
                in.nextLine();
                TCPHandler tcpHandler = new TCPHandler(choosenAddress, portNumber);
                tcpHandler.start();
            }
        }
    }
}
