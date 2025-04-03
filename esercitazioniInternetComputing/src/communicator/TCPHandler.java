package communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPHandler extends Thread{
    private Socket socket;
    private String address;
    private int port;

    public TCPHandler(String address, int port){
        this.address = address;
        this.port = port;
        try{
            this.socket = new Socket(address, port);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void run(){
        PrintWriter out;
        BufferedReader in;
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            boolean done = false;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Benvenuto nella chat con " + address);
            while(!done){
                String input = scanner.nextLine();
                out.println(input);
                if(input.equals("exit")){
                    done = true;
                    out.println("User has disconnected");
                }else{
                    String response = in.readLine();
                }
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
