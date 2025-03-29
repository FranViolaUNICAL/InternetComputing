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

## Guida - Far Interagire due Applicazioni Su Reti Domestiche Diverse

Quando due applicazioni devono comunicare su computer situati in reti domestiche diverse, è necessario configurare il router per consentire il traffico su una specifica porta. Segui questi passaggi per abilitare la connessione.

### 1. Identificare l'Indirizzo IP Locale
Ogni computer nella rete ha un indirizzo IP privato. Per trovarlo:
- Su Windows: Apri il prompt dei comandi e digita `ipconfig`, quindi cerca l'"Indirizzo IPv4".
- Su macOS/Linux: Apri il terminale e digita `ifconfig` o `ip a`.

### 2. Configurare il Port Forwarding sul Router
Il port forwarding consente alle connessioni esterne di raggiungere un dispositivo specifico nella rete locale.
1. Accedi all'interfaccia web del router (generalmente tramite `http://192.168.1.1` o `http://192.168.0.1`).
2. Accedi con le credenziali amministrative.
3. Cerca la sezione "Port Forwarding" o "Virtual Server".
4. Aggiungi una nuova regola:
   - Porta esterna: la porta su cui il client si connetterà (es. 5000)
   - Indirizzo IP interno: l'IP del computer che ospita l'applicazione
   - Porta interna: la porta su cui l'applicazione è in ascolto (es. 5000)
   - Protocollo: TCP, UDP o entrambi
5. Salva le impostazioni e riavvia il router se necessario.

### 3. Trovare l'Indirizzo IP Pubblico
Il dispositivo che si connette dall'esterno deve conoscere l'IP pubblico del router:
- Visita un sito come `https://www.whatismyip.com/` per trovare l'IP pubblico.
- Questo indirizzo può cambiare nel tempo se non si ha un IP statico.

### 4. (Opzionale) Usare un Servizio Dynamic DNS
Se l'IP pubblico cambia frequentemente, si può usare un servizio di Dynamic DNS (DDNS) per assegnare un nome di dominio statico.
1. Registrati su un servizio DDNS come No-IP o DynDNS.
2. Configura il router con le credenziali DDNS (spesso nella sezione "DDNS" del router).
3. Usa il dominio assegnato per connetterti invece dell'IP pubblico.

### 5. Testare la Connessione
- Dal dispositivo remoto, prova a connetterti usando `telnet [IP_pubblico] [porta]` o strumenti come `nc` su Linux/macOS.
- Se l'applicazione usa HTTP, prova ad aprire il browser su `http://[IP_pubblico]:[porta]`.
- Se la connessione non funziona, verifica il firewall del computer e le impostazioni del router.

Seguendo questi passaggi, le due applicazioni dovrebbero essere in grado di comunicare tra loro anche se situate in reti domestiche diverse.

## Datagrammi
Le applicazioni che comunicano tramite socket possiedono un canale di comunicazione dedicato. Per comunicare, un client ed un server stabiliscono una connessione, trasmettono dati e chiudono la connessione. Tutti i dati inviati sul canale sono ricevuti nello stesso ordine in cui sono stati inviati. 

Le applicazioni che comunicano tramite datagrammi inviano e ricevono pacchetti di informazione completamente indipendenti fra di loro. Queste applicazioni non dispongono e non necessitano di un canale di comunicazione punto-a-punto. La consegna dei datagrammi alla loro destinazione non é garantita e neppure l'ordine del loro arrivo. 

Il package `java.net` fornisce delle classi che consentono di scrivere applicazioni che usano datagrammi per inviare e ricevere pacchetti sulla rete: `DatagramSocket` e `DatagramPacket`. Una applicazione puó inviare e ricevere `DatagramPacket` tramite un `DatagramSocket`. 

## UDP (Richiamo)
UDP é un protocollo di livello di trasporto a pacchetto. Esso é di tipo "connectionless", quindi non gestisce4 il riordinamento dei pacchetti né la ritrasmissione di quelli persi. É molto rapido poiché non c'é latenza per riordino e ritrasmissione, ed é usato da applicazioni "leggere" o time-sensitive: trasmissione di informazioni audio-video real-time come ad esempio Voip. 

### `java.net.DatagramSocket`

```Java
// Crea un DatagramSocket e lo collega alla porta qualsiasi sulla macchina locale. Solleva eccezione se non si hanno i permessi necessari.
DatagramSocket() throws SocketException;
// Crea un DatagramSocket e lo collega alla porta specificata sulla macchina locale
DatagramSocket(int port);
// Riceve un DatagramPacket da questo socket
void receive(DatagramPacket p);
// Invia un DatagramPacket su questo socket
void send(DatagramPacket p);
// Chiude il DatagramSocket
void close();
// Crea un DatagramPacket per ricevere pacchetti di lunghezza length, che deve essere inferiore a bug.length
DatagramPacket(byte[] buf, int length);
// Crea un DatagramPacket per inviare pacchetti di lunghezza length all'host ed alla porta specificati
DatagramPacket(byte[] buf, int length, InetAddress address, int port);
// Restituisce l'indirizzo IP della macchina alla quale questo DatagramPacket deve essere inviato o da cui é stato ricevuto
InetAddress getAddress();
// Restituisce la porta della macchina alla quale questo DatagramPacket deve essere inviato o da cui é stato ricevuto
int getPort();
```


