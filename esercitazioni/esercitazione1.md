# Esercitazione 1 (Socket)

## Protocolli di rete
Un protocollo definisce il formato e l'ordine dei messaggi scambiati fra due o piú entitá in comunicazione. Un insieme di protocollo é detto pila di protocolli. ISO-OSI é composto da 7 livelli, mentre il modello TCP-IP é composto da 4 livelli.

## Definizioni
Un nodo **host** indica ogni terminale collegato, attraverso link di comunicazione, ad una rete informatica. 

Un Indirizzo IP é un'etichetta numerica che identifica univocamente un host collegato a una rete informatica che utilizza l'Internet Protocol come protocollo di rete.

L'**hostname** é il nome identificativo di un dispositivo all'interno di una rete di calcolatori. Puó far parte di un URL. Su Internet l'hostname permette di associare una denominazione ad un indirizzo IP.

Un **API** (Application Programming Interface) si indica un insieme di procedure disponibili al programmatore, di solito raggruppate a formare un set di strumenti specifici per l'espletamento di un determinato compito all'interno di un programma.

Un **server** é un computer di elevate prestazioni che in una rete fornisce un servizio agli altri elaboratori collegati, detti **client**.

Un **protocollo** é una descrizione formale del formato dei messaggi e delle regole che due elaboratori devono adottare per lo scambio dei messaggi.

**Tracerouter o tracert** é un programma diagnostico che fornisce una misura del ritardo della sorgente al router lungo i percorsi Internet punto-punto verso la destinazione. Invia 3 pacchetti che raggiungeranno il router $i$ sul percorso verso la destinazione. Il router $i$ restituirá i pacchetti al mittente e il mittente calcola l'intervallo tra trasmissione e risposta. Il comando si invoca con tracert su Windows e traceroute su sistemi MACOS e LINUX.

## Package java.net
Fornisce le classi per implementare applicazioni di rete. Puó essere suddiviso in 2 sezioni:
1. Low Level API:
    1. Address: identificatori di rete (Indirizzi IP mediante InetAddress ad ex.)
    2. Socket: meccanismi di base di comunicazione bidirezionale
    3. Interface: interfacce di rete (es. la classe NetworkInterface usata per interagire con la scheda di rete del computer)
2. High Level API:
    1. URL: Univeral Resource Locator, rappresenta un puntatore ad una "risorsa" sul WWW
    2. Connection: Le connessioni alla risorsa puntata dall'URL

### java.net.InetAddress

```Java
// restituisce l'IP di un dato host (IPv4 o IPv6)
static InetAddress getByName(String host)
// restituisce tutti gli indirizzi IP di un dato host
static InetAddress[] getAllByName(String host)
// restituisce l'indirizzo IP di localhost
static InetAddress getLocalHost()
// restituisce l'indirizzo IP sotto forma di array di byte.
byte[] getAddress()
// restituisce l'indirizzo IP sottoforma di stringa.
String getHostAddress()
// restituisce l'hostname associato all'indirizzo IP.
String getHostName() 
```

### Lookup di Indirizzi locali e remoti

```Java
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
```

## Host name-to-IP resolution
Viene realizzata attraverso l'uso di una combinazione di informazioni ottenute attraverso il Domain Name System (DNS) e informazioni locali. Le informazioni locali possono provenire da cartelle come /etc/hosts in UNIX o c:\WINDOWS\system32\drivers\etc\hosts su WINDOWS.

Il DNS é il sistema dei nomi di dominio, ovvero un sistema utilizzato per la risoluzione dei nomi dei nodi della rete (host) in indirizzi IP e viceversa. Il servizio é realizzato tramite un database distribuito costituito dai server DNS.

## Java.io
In Java input e output sono definiti tramite flussi (stream), ovvero sequenze ordinate di dati. Vi sono 2 tipi di flussi:
1. Di dati binari (byte stream)
2. Di caratteri (character stream).

I flussi di input hanno una sorgente:
1. Per dati binari, usare la classe InputStream
2. Per caratteri, usare la classe Reader

I flussi di output hanno una destinazione:
1. per dati binari, usare la classe OutputStream
2. per caratteri, usare la classe Writer.

### Lettura
La classe astratta InputStream tramite il metodo read() legge un byte alla volta. La classe astratta Reader tramite il metodo read() legge un carattere alla volta.

La classe concreta InputStreamReader permette di convertire un flusso di input binario in un flussi di input di caratteri. La classe concreta BufferedReader tramite readLine() legge un carattere alla volta fino ad ottenere una linea.

La classe astratta OutputStream tramite il metodo write() scrive un byte alla volta. La classe astratta Writer tramite il metodo write() scrive un carattere alla volta.

La classe concreta OutputStreamWriter permette di convertire un flusso di output di caratteri in un flusso di output binario. La classe concreta PrintWriter tramite println() scrive numeri e stringhe in singoli caratteri.

## Socket
Il socket é un canale di comunicazione che permette a due processi che lavorano su due macchine fisicamente separate (o sulla stessa macchina) di comunicare fra di loro.

Dal punto di vista di un programmatore, il socket é un particolare oggetto sul quale leggere e/o scrivere dati da trasmettere o ricevere.

## Porte remote TCP
Sono uno strumento utilizzato per realizzare la multiplazione delle connessioni a livello di trasporto, ovvero per permettere ad un calcolatore di effettuare piú connessioni contemporanee verso altri calcolatori. 

Le porte sono numeri (TCP e UDP usano 16 bit per numerarle, per un totale di 65535 porte) utilizzate per identificare una particolare connessione di trasporto tra quelle al momento attive su un calcolatore. Le porte note (known ports) sono le porte TCP e UDP nell'intervallo 0-1023 e sono assegnate a specifici servizi della IANA (Internet Assigned Numbers Agency).

### java.net.Socket

```Java
// restituisce lo stream di input del socket; utilizzato per leggere i dati provenienti dal socket
InputStream getInputStream();
// restituisce lo stream di output del socket; utilizzato per scrivere dati nel socket
OutputStream getOutputStream();
// imposta il timeout per operazioni di lettura dal socket; se il tempo specificato trascorre viene lanciata una InterruptedIOException
void setSoTimeout(int timeout);
// restituisce una rappresentazione del socket del tipo "Socket[addr=hostname/192.168.90.82,port=3575,localport=1026]"
String toString();
```

## Telnet
Telnet é un protocollo di rete utilizzato su Internet. É usato per fornire all'utente sessioni di login remoto di tipo riga di comando tra host su Internet.

## Daytime Protocol
É uno standard internet del 1983. É un utile strumento di debug e misurazione del tempo. Un servizio daytime invia semplicemente la data e l'ora correnti come stringa di caratteri, indipendentemente dall'input.

Un servizio daytime é definito come applicazione basata su TCP. Un server ascolta le connessioni TCP sulla porta TCP 13. Una volta stabilita una connessione, la data e l'ora correnti vengono inviate come una stringa di caratteri ASCII (e tutti i dati ricevuti vengono eliminati). Il servizio chiude la connessione dopo l'invio del preventivo.

Non esiste una sintassi specifica, ma si consiglia di limitarsi ai caratteri di stampa ASCII, allo spazio, al ritorno a capo e all'avanzamento riga. Il giorno dovrebbe essere solo una riga.

### Connessione ad un time-server con Java
```Java
 public static void main(String[] args){
        try{
            Socket s = new Socket("ntp1.inrim.it",13);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            boolean more = true;
            while(more){
                String line = in.readLine();
                if(line == null){
                    more = false;
                }
                else{
                    System.out.println(line);
                }
            }
        }catch(IOException e) {
            System.out.println(e);
        }
    }
```

## Protocollo HTTP
Il protocollo HTTP (HyperText Transfer Protocol) é un protocollo usato per la trasmissione d'informazioni sul web. Un server HTTP generalmente resta in ascolto sulla porta 80 usando il protocollo TCP a livello di trasporto. Funziona su un meccanismo di richiesta/risposta: il client effettua una richiesta e il server restituisce una risposta. Vi sono due tipi di messaggi HTTP: messaggi richiesta e messaggi risposta.

Il messaggio di richiesta é composto da 3 parti e deve terminare con una riga vuota, cioé due a capo consecutivi:
1. Riga di richiesta (request line) composta da metodo (es. GET), URI e versione del protocollo
2. sezione Header (informazioni aggiuntive)
3. sezione Body (corpo del messaggio)

   `GET /wiki.com/Pagina.principale HTTP/1.1`

Il messaggio di risposta e di tipo testuale ed é composto da tre parti:
1. Riga di stato (status-line)
2. sezione Header
3. sezione Body (contenuto della risposta).

```HTTP
HTTP/1.0 200 OK
Date: Mon, 28 Jun 2004 10:47:31 GMT
Server: Apache/1.3.29 (Unix) PHP/4.3.4
Content-Language: it
Content-Type: text/html; charset=utf-8
Age: 7673
Connection: close
```

### Codici di Status HTTP
1. 2xx Success
    1. 200 OK: risposta standard
2. 3xx Redirezione
    1. 301 Moved Permanently: Questa e tutte le future richieste andranno dirette ad un altro URI (specificato nell'header Location)
3. 4xx Client Error
    1. 400 Bad Request: La richiesta non puó essere soddisfatta a causa di errori di sintassi;
    2. 404 Not Found: La risorsa richiesta non é stata trovata ma in futuro potrebbe essere disponibile;
4. 5xx Server Error
    1. 500 Internal Server Error: Messaggio di errore generico
    2. 503 Service Unavailable: Il server non é al momento disponibile, generalmente é una condizione temporanea.
  
### Download di una paequalsgina HTML da Java
```Java
import java.io.*;
import java.net.*;
public class HTMLDownloader {
    public static void main(String[] args){
        try{
            String URL = "stackoverflow.com";
            Socket s = new Socket(URL, 80);
            s.setSoTimeout(10000);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println("GET /questions HTTP/1.1");
            out.println("Host: " + URL);
            out.println("");
            boolean more = true;
            while(more){
                String line = in.readLine();
                if(line == null) more = false;
                else System.out.println(line);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
      }
    }
}
```
## SocketOpener

```Java
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
```
## java.net.ServerSocket
```Java
// crea un server socket che controlla una porta
ServerSocket(int port);
// rimane in attesa di una connesione, e restituisce un socket tramite il quale si effettua la comunicazione
Socket accept();
// chiude il server socket;
void close();
// restituisce l'indirizzo locale di questo server socket
InetAddress getInetAddress();
// restituisce la porta locale di questo server socket
int getLocalPort();
```

### EchoServer in Java
```Java
import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String[] args){
        try{
            ServerSocket s = new ServerSocket(8189);
            Socket incoming = s.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            PrintWriter out = new PrintWriter(incoming.getOutputStream(),true);
            out.println("Hello! Enter BYE to exit.");

            boolean done = false;
            while(!done){
                String line = in.readLine();
                if(line == null){
                    done = true;
                }else{
                    out.println("Echo: " + line);
                    if(line.trim().equals("BYE")){
                        done = true;
                    }
                }
            }
            incoming.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### BufferedReader e PrintWriter
Il BufferedReader puó essere usato per leggere da un socket una stringa di input.
`String s = br.readLine();`

Deve ricevere una stringa che contiene il ritorno a capo. Infatti il metodo readLine() blocca l'applicazione fin quando essa non riceve un carattere di ritorno a capo o se la connessione viene chiusa.

PrintWriter puó essere usato invocando il metodo:
`pw.println("...");`

Usando i metodi pw.print(...) o pw.write(...) possono generare questo tipo di blocco.

## Serializzazione
La serializzazione é un processo per salvare un oggetto in un supporto di memorizzazione (ad es. un file) o per trasmetterlo su una connessione di rete. La serializzazione di un oggetto puó avvenire in forma binaria o puó utilizzare codifiche testuali (XML o JSON) direttamente leggibili da utenti umani.

Lo scopo della serializzazione é di trasmettere l'intero stato dell'oggetto in modo che esso possa essere successivamente ricreato nello stesso identico stato dal processo inverso, detto deserializzazione.


