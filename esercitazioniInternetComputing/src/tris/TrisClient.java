package tris;
import java.net.*;
import java.io.*;
import java.util.*;

public class TrisClient {
    public final String SERVER_ADDRESS = "localhost";
    public final int SERVER_PORT = 5000;
    public static void main(String[] args) {
        try{
            TrisClient client = new TrisClient();
            Socket socket = new Socket(client.SERVER_ADDRESS, client.SERVER_PORT);
            System.out.println(socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String input = in.readLine();
            System.out.println(input);
            Scanner scanner = new Scanner(System.in);
            String ins = scanner.nextLine();
            new Thread(() -> {
                while(true){
                    out.println(ins);
                    String message = null;
                    try {
                        message = in.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(message);
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
