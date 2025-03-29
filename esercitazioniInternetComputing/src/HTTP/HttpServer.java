package HTTP;
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpServer {
    private static final int PORT = 3575;
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Listening on port " + PORT);
        while(true){
            Socket socket = server.accept();
            ThreadedServer cc = new ThreadedServer(socket);
        }
    }
}

class ThreadedServer extends Thread{
    private Socket client;
    private BufferedReader is;
    private DataOutputStream os;
    public ThreadedServer(Socket s){
        client = s;
        try{
            is = new BufferedReader(new InputStreamReader(client.getInputStream()));
            os = new DataOutputStream(client.getOutputStream());
        }catch(IOException e){
            try{
                client.close();
            }catch(IOException ioe){
                System.err.println(""+ioe);
            }
            return;
        }
        this.start();
    }

    public void run(){
        try{
            String request = is.readLine();
            System.out.println(request);
            StringTokenizer st = new StringTokenizer(request);
            if((st.countTokens() >= 2) && st.nextToken().equals("GET")){
                if((request=st.nextToken()).startsWith("/")){
                    request = request.substring(1);
                }
                if(request.endsWith("/") || request.equals("")){
                    request = request+"index.html";
                }
                if((request.indexOf("..")!= -1) || (request.startsWith("/"))){
                    os.writeBytes("403 Forbidden. " + "You do not have enough privileges to read: " +request+"\r\n");
                }else{
                    File f = new File(request);
                    reply(os,f);
                }
            }else{
                os.writeBytes("400 Bad Request\r\n");
            }
            client.close();
        }catch (IOException e1) {
            System.err.println(""+e1);
        }catch (Exception e2){
            System.err.println(""+e2);
        }
    }

    public static void reply(DataOutputStream out, File f) throws IOException{
        try{
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            int len = (int) f.length();
            byte buf[] = new byte[len];
            in.readFully(buf);
            out.writeBytes("HTTP/1.0 200 OK\r\n");
            out.writeBytes("Content-Length: "+buf.length+"\r\n");
            out.writeBytes("Content-Type: text/html\r\n\r\n");
            out.write(buf);
            out.flush();
            in.close();
        }catch(FileNotFoundException e){
            out.writeBytes("404 Not Found\r\n");
        }
    }
}
