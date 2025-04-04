package echoServer;

import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String[] args){
        try{
            ServerSocket s = new ServerSocket(8189); // è sottinteso che poi i client usano una socket normale che si collega con l'indirizzo IP del server
            Socket incoming = s.accept();
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            PrintWriter out = new PrintWriter(incoming.getOutputStream(),true);
            out.println("Hello! Enter BYE to exit.");

            boolean done = false;
            while(!done){
                String line = in.readLine();
                if(line == null){
                    done = true;
                }else{
                    out.println("Echo: " + line);
                    if(line.trim().equals("BYE")){
                        done = true;
                    }
                }
            }
            incoming.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
