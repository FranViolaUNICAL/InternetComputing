package serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ReceiveObjectAdv {
    public static void main(String[] args){
        try{
            List<Studente> l = new ArrayList<>();
            Socket socket = new Socket("localhost",3575);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            boolean done = false;
            while(!done){
                Studente studente = (Studente) input.readObject();
                if(l.contains(studente)){
                    done = true;
                }else{
                    l.add(studente);
                    output.println("Siamo spiacenti ma non é presente nessun studente con la matricola inviata.");
                }
            }
            socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
