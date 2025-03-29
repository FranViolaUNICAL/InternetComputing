package serialization;

import java.io.*;
import java.net.*;

public class ReceiveObject {
    public static void main(String[] args){
        try{
            Socket socket = new Socket("localhost",3575);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            String beginMessage = (String) input.readObject();
            System.out.println(beginMessage);
            Studente studente = (Studente) input.readObject();
            System.out.print(studente.getMatricola() + " - ");
            System.out.print(studente.getNome() + " - ");
            System.out.print(studente.getCognome() + " - ");
            System.out.print(studente.getCorsoDiLaurea() + "\n");
            String endMessage = (String)input.readObject();
            System.out.println(endMessage);
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
