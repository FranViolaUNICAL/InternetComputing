package HTTP;
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpWelcomeClient {
    private static String getRequest() {
        return "GET /welcome.html HTTP/1.0\n\n";
    }

    public static void main(String[] args) {
        try{
            Socket s = new Socket("localhost",80);
            PrintWriter out = new PrintWriter(s.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            boolean done = false;
            while(!done){
                out.println(getRequest());
                System.out.println("Sent request. Waiting for response...");
                boolean more = true;
                while(more){
                    String line = in.readLine();
                    if(line == null){
                        more = false;
                        done = true;
                        break;
                    }else{
                        System.out.println(line);
                    }
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
