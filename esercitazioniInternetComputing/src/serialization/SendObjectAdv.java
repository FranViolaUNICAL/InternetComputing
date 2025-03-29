package serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SendObjectAdv {
    public static void main(String[] args){
        try{
            ServerSocket server = new ServerSocket(3575);
            Socket client = server.accept();
            ObjectOutputStream output =  new ObjectOutputStream(client.getOutputStream());
            Studente studente = new Studente(14250, "Leonardo", "Da Vinci", "Ing. Informatica");
            output.writeObject(studente);
            Studente studente1 = new Studente(13456, "Leonardo", "Da Vinci", "Ing. Informatica");
            Studente studente2 = new Studente(56462, "Leonardo", "Da Vinci", "Ing. Informatica");
            Studente studente3 = new Studente(234558, "Leonardo", "Da Vinci", "Ing. Informatica");
            output.writeObject(studente);
            output.writeObject(studente);
            output.writeObject(studente);
            client.close();
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
