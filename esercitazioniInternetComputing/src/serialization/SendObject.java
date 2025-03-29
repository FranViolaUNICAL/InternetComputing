package serialization;

import java.io.*;
import java.net.*;

public class SendObject {
    public static void main(String[] args){
        try{
            ServerSocket server = new ServerSocket(3575);
            Socket client = server.accept();
            ObjectOutputStream output =  new ObjectOutputStream(client.getOutputStream());
            output.writeObject("<Welcome>");
            Studente studente = new Studente(14250, "Leonardo", "Da Vinci", "Ing. Informatica");
            output.writeObject(studente);
            output.writeObject("<Goodbye>");
            client.close();
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
