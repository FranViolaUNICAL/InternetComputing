package echoServer;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoServerClient {
    public static void main(String[] args){
        try{
            Socket s = new Socket("localhost",8189);
            if(s == null){
                System.out.println("Could not create socket");
            }else{
                System.out.println(s);
            }
            Scanner sc = new Scanner(System.in);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            System.out.println("Scrivi un messaggio (BYE per uscire)");

            while(true){
                System.out.println(">");
                String userInput = sc.nextLine();

                out.println(userInput);
                if("BYE".equalsIgnoreCase(userInput)){
                    break;
                }

                String response = in.readLine();
                if (response == null){
                    System.out.println("Connessione chiusa dal server.");
                    break;
                }
                System.out.println("Echo: " + response);
            }

            s.close();
            System.out.println("Connessione chiusa!");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
