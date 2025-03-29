package HTTP;
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpWelcome {
    private static int port = 80;
    private static String HtmlWelcomeMessage(){
        return "<html>\n" +
                " <head>\n " +
                " <title> UNICAL - Ingegneria Informatica </title>\n"+
                " </head>\n" +
                " <body>\n " +
                " <h2 align=\"center\">\n" +
                "  <font color=\"#0000FF\"> Benvenuti al Corso di" +
                " Reti di Calcolatori</font>\n"+
                "  </h2>\n"+
                " </body>\n" +
                " </html>";
    }
    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(port);
            System.out.println("Listening on port " + port);
            while(true){
                Socket client = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                String request = in.readLine();
                System.out.println(request);
                StringTokenizer st = new StringTokenizer(request);
                if((st.countTokens()>=2) && st.nextToken().equals("GET")){
                    String message = HtmlWelcomeMessage();
                    //start of response headers
                    out.println("HTTP/1.0 200 OK");
                    out.println("Content-Length: " + message.length());
                    out.println("Content-Type: text/html");
                    out.println();
                    //end of response headers
                    out.println(message);
                }else{
                    out.println("400 Bad Request");
                }
                out.flush();
                client.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
