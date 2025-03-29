# Esercitazione 2

## HTTP (Richiamo)

L'HTTP è un protocollo usato per la trasmissione d'informazioni sul web. Livello Applicazione. Un server HTTP generalmente resta in ascolto sulla porta 80 usando il protocollo TCP a livello di trasporto.

L'HTTP funziona su un meccanismo richiesta/risposta (client/server): il client effettua una richiesta e il server restituisce la risposta. Il client corrisponde al browser ed il server al sito web. Vi sono quindi due tipi di messaggi HTTP: messaggi richiesta e messaggi risposta.

### HTTP Request
Il messaggio di richiesta è composto da tre parti:
1. Riga di richiesta (request line) composta da metodo, URI e versione del protocollo
2. Sezione header (informazioni aggiuntive)
3. Body (corpo del messaggio)

La richiesta deve terminare con due "a capo" consecutivi.

```HTTP
GET /wiki.com/Pagina_principale HTTP/1.1
```

### HTTP Response
Il messaggio di risposta è di tipo testuale ed è composto da tre parti:
1. riga di stato (status-line)
2. sezione header
3. body (contenuto della risposta)

```HTTP
HTTP/1.0 200 OK
Date: Mon, 28 Jun 2004 10:47:31 GMT
Server: Apache/1.3.29 (Unix) PHP/4.3.4
Content-Language: it
Content-Type: text/html; charset=utf-8
Age: 7673
Connection: close
```

### HTTPWelcome

```Java
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
```

### BufferedWriter

Il metodo flush() svuota il flusso di output e forza la scrittura di tutti i byte di output bufferizzati. Flush() dovrebbe essere chiamato se alcuni byte precedentemente scritti sono stati bufferizzati dall'implementazione del flusso di output.

La classe PrintWriter mette a disposizioni diversi costruttori, tra cui:

1. `PrintWriter(OutputStream out)`: Crea un nuovo PrintWriter, senza il flushing automatico, da una OutputStream esistente. Il flush va attivato manualmente
2. `PrintWriter(OutputStream out, boolean autoFlush)`: Crea un nuovo PrintWriter da una OutputStream esistente, con flush automatico ogni qualvolta viene invocato il metodo println, printf o format.

## HttpServer
Controllare file di codice sorgente per implementazione.

La gestione della richiesta avviene in maniera del tutto analoga a quanto visto nei paragrafi precedenti. La differenza sta nell'aggiunta di codice che estrae l'URI della risorsa richiesta e la processa per evitare che l'utente possa richiedere risorse fuori dalla **web root**.

Questo lo facciamo nel seguente modo:
1. se l'URI della risorsa richiesta termina con "/" o "", allora aggiungiamo di defaul "index.html".
2. Se la richiesta contiene ".." o inizia con "/" si restituisce un errore 403 Forbidden.

In questo modo si evitano situazioni in cui si possa realizzare una "folder escalation" usando ".." nell'URI o magari richiedere un path assoluto del sistema (ad es. il file con le password di tutti gli utenti). Se non si rientra nelle situazioni di errore segnalate sopra, il thread carica il file della risorsa richiesta e lo restituisce al client utilizzando il metodo `reply()` a cui passa l'output stream ed il file della risorsa.

