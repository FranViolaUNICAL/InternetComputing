package sockets;

import java.io.*;
import java.net.*;

public class SocketOpenerTest {
    public static void main(String[] args){
        String host = "www.dimes.unical.it";
        int port = 80;
        int timeout = 10000;
        Socket s = SocketOpener.openSocket(host,port,timeout);
        if(s == null){
            System.out.println("The socket could not be openend.");
        }else{
            System.out.println(s);
        }
    }

    static class SocketOpener extends Thread {
        private String host;
        private int port;
        private Socket socket;

        public static Socket openSocket(String host, int port, int timeout){
            SocketOpener opener = new SocketOpener(host, port);
            opener.start();
            try{
                opener.join(timeout);
            }catch(InterruptedException e){
                System.err.println(e);
            }
            return opener.getSocket();
        }

        public SocketOpener(String host, int port){
            this.host = host;
            this.port = port;
            socket = null;
        }

        public Socket getSocket() {
            return socket;
        }

        public void run(){
            try{
                socket = new Socket(host,port);
            }catch (IOException e){
                System.err.println(e);
            }
        }
    }
}
