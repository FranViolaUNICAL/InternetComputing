package misc;

import java.net.*;

public class Lookup {
    static void printLocalAddress(){
        try{
            InetAddress myself = InetAddress.getLocalHost();
            System.out.println("My name: " + myself.getHostName());
            System.out.println("My IP: " + myself.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    static void printRemoteAddress(String name){
        try{
            InetAddress machine = InetAddress.getByName(name);
            System.out.println("Host name: " + machine.getHostName());
            System.out.println("Host IP: " + machine.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
