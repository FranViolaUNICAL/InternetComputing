package sockets;

import java.io.*;
import java.net.*;
public class HTMLDownloader {
    public static void main(String[] args){
        try{
            String URL = "stackoverflow.com"; //
            Socket s = new Socket(URL, 80);
            // Socket s = new Socket(INDIRIZZO.IP, PORTA) oppure new Socket(hostname, porta) dove hostname è tipo stackoverflow.com o localhost
            s.setSoTimeout(10000);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true); // il flush si assicura che sostanzialmente tutti i dati vengano inviati prima di passare al dato successivo
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println("GET /questions HTTP/1.1"); // questa richieste chiede a stackoverflow.com di inviare a questo client la pagina stackoverflow.com/questions
            out.println("Host: " + URL); // questo fa parte dell'header della richiesta
            out.println(""); // questo ci permette di chiudere la richiesta
            // da base una richiesta HTTP si chiude con un carattere vuoto
            /*
            GET /questions HTTP/1.1
            Host: stackoverflow.com

             */
            boolean more = true; // la pagina è formata da più righe
            //quindi finchè il BufferedReader sa che ci sono altre righe, le continuo a stampare
            while(more){
                String line = in.readLine();
                if(line == null) more = false; // se la ln che ho trovato è nulla allora mi fermoe
                else System.out.println(line);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
