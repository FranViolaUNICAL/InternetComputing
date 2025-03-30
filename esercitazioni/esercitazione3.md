# Esercitazione 3
## Multicast (Richiamo)
Il termine multicast viene utilizzato per indicare la distribuzione simultanea di informazione verso un gruppo di destinatari. (one-to-many o many-to-many).

A livello di rete locale, una classe di indirizzi ethernet é riservata all'uso come indirizzi multicast (224.0.0.0 - 239.255.255.255). Questi pacchetti sono trattati dalla rete come se fossero broadcast, ovvero sono ritrasmessi a tutti i computer collegati.

Per il multicast vi sono indirizzi riservati (da RFC 1112 e altre)
1. 224.0.0.0 non é usato da alcun gruppo.
2. 224.0.0.1 é il gruppo formato da tutti gli host della LAN.
3. 224.0.0.2 é il gruppo di tutti i routers.
4. 224.0.0.4 é il gruppo di tutti i DVMRP (Distance Vector Multicast Routing Protocol) routers.
5. 224.0.0.5 é il gruppo degli OSPF (Open Shortest Path First) routers.
6. 224.0.0.13 é il gruppo dei PIM (Protocol Independent Multicast) routers.
7. 224.0.0.0 - 224.0.0.255 é riservato a utilizzi locali (amministrazione e manutenzione) e i datagrammi con queste destinazioni non sono mai inoltrati dai multicast routers.
8. 224.0.1.1 é riservato per il NTP (Network Time Protocol)

Il concetto di Multicast é stato ideato per facilitare la comunicazione efficiente tra gruppi di host all'interno di una rete di calcolatori. É molto usato nel caso di trasmissioni di stream multimediali a gruppi di host.

I computer che vogliono ricevere le "trasmissioni" del gruppo multicast si devono registrare a quel gruppo. La rete si occuperá di consegnare i pacchetti multicast a tutti quelli che si sono registrati. Spesso non c'é modo di controllare ne chi trasmette su un gruppo multicast, né quali computer possono ricevere, se non in modo piuttosto grossolano.

Per la natura del servizio di rete multicast, risulta molto difficile usare protocolli di trasporto orientati alla connessione come TCP, per cui si usano protocolli senza connessione come UDP.

Il multicast é una modalitá di comunicazione poco controllabile e si basa sull'uso di protocolli connectionless (es. UDP).

## java.net.MulticastSocket

MulticastSocket é un estensione di DatagramSocket. Funziona come un DatagramSocket ma con funzionalitá aggiuntive per unirsi a gruppi o altri host multicast sulla rete.

Un gruppo multicast é identificato da un indirizzo IP e da una porta standard UDP. É utile per spedire e ricevere pacchetti IP multicast.

```Java
//Crea un MulticastSocket e lo collega alla porta specificata sulla macchina locale
MulticastSocket(int port);
//Si collega ad un multicast group
void joinGroup(InetAddress mcastaddr);
//abbandona un multicast group
void leaveGroup(InetAddress mcastaddr);
//Riceve un DatagramPacket da questo socket;
void receive(DatagramPacket p);
//Invia un DatagramPacket da questo socket;
void send(DatagramPacket p);
// chiude questo multicastSocket;
void close();
```

### MulticastTimeServer
```Java
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class MulticastTimeServer {
    public static void main(String[] args) {
        MulticastSocket socket = null;
        try{
            socket = new MulticastSocket(3575);
            while(true){
                byte[] buf = new byte[256];
                // non aspetta la richiesta
                String dString = new Date().toString();
                buf = dString.getBytes();
                // invia messaggio in broadcast
                InetAddress address = InetAddress.getByName("230.0.0.1");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 3575);
                socket.send(packet);
                System.out.println(dString);
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
            socket.close();
        }
    }
}
```

### MulticastTimeClient
```Java
public class MulticastTimeClient {
    public static void main(String[] args) {
        MulticastSocket socket = null;
        try{
            socket = new MulticastSocket(3575);
            InetAddress group = InetAddress.getByName("230.0.0.1");
            socket.joinGroup(group);
            DatagramPacket packet;
            for(int i = 0; i < 100; i++){
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            }
            socket.leaveGroup(group);
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
```

## java.net.URL
```Java
//crea un oggetto URL a partire dalla stringa che lo rappresenta
URL(String spec);
//Crea un URL a partire dai componenti specificati, se port=-1 indica l'uso della porta standard per il protocollo specificato
URL(String protocol, String host, int port, String file);
//Restituisce un oggetto URLConnection che gestisce la connessione diretta alla risorsa
URLConnection openConnection();
//Apre un flusso di input per la lettura dei dati della risorsa
InputStream openStream();
// restituisce la query Strig di un URL
String getQuery();
// restituisce la reference o anchor di un URL
String getRef();
// restituisce l'hostname di un URL
String getHost();
// restituisce la porta di un URL
String getPort();
// restituisce il protocollo di un URL
String getPrococol();
// restituisce la parte di informazioni utente di un URL
String getUserInfo();
```

Mimica `<protocol>://<userinfo>@<hostname>:<port>/<path>?<query>#<references>`

### Recupero del contenuto di un sito Web

```Java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class URLTest {
    public static void main(String[] args) {
        try{
            URL url = new URL("http://www.w3.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            boolean more = true;
            while(more){
                String line = in.readLine();
                if(line == null){
                    more = false;
                }else{
                    System.out.println(line);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

### java.net.URLConnection
Per ottenere dalla risorsa delle informazioni aggiuntive e controllarne meglio l'accesso si puó utilizzare la classe URLConnection. In generale creare una URLConnection significa:
1. Chiamare il metodo openConnection di un oggetto URL (`URLConnection connection = url.openConnection()`)
2. Impostare le proprietá usando i metodi setDoInput, setDoOutput, setIfModifiedSince, setUseCaches, setAllowUserInteraction, setRequestProperty
3. Effettuare la connessione alla risorsa remota mediante il metodo connect `connection.connect();` che crea una connessione socket con il server e richiede al server le informazioni di intestazione.
4. Dopo aver attivato la connessione é possibile richiedere le informazioni di intestazione. I metodi getHeaderFieldKey e getHeaderField consentono di accedere a tutti i campi di intestazione. I seguenti metodi interroganoi i campi standard:
   1. getContentType
   2. getContentLength
   3. getContentEncoding
   4. getDate
   5. getExpiration
   6. getLastModified
5. Si accede alla risorsa remota utilizzando il metodo `getInputStream` per ottenere un flusso di input per leggere le informazioni, ed il metodo `getOutputStream` per ottenere un flusso di output per inviare informazioni.

```Java
// se doInput é true, l'utente puó ricevere l'input da questo URLConnection
void setDoInput(boolean doInput);
// se doOutput é true, l'utente puó ricevere l'output da questo URLConnection
void setDoOutput(boolean doOutput);
// configura questo URLConnection per recuperare solo i dati che sono stati modificati dopo la data indicata
void setIfModifiedSince(long time);
// se useCaches é true, i dati possono essere recuperati da una cache locale
void setUseCaches(boolean useCaches);
// se allowUserInteraction é true, all'utente puó essere richiesto un input addizionale (come password ec.)
void setAllowUserInteraction(boolean allowUserInteraction);
// imposta una proprietá della richiesta
void setRequestProperty(String key, String value);
// si connette alla risorsa remota e recupera le informazioni di intestazione
void connect();
// recupera il tipo di contenuto, per esempio text/plain o image/gif
String getContentType();
// recupera la lunghezza del contenuto, oppure -1 se il valore é sconosciuto
int getContentLength();
// recupera la codifica del contenuto, ad esempio gzip
String getContentEncoding();
// recupera la data di creazione della risorsa
long getDate();
// recupera la data di scadenza della risorsa
long getExpiration();
// recupera la data dell'ultima modifica della risorsa
long getLastModified();
// recupera l'n-esima chiave del campo di intestazione
String getHeaderFieldKey(int n);
// recupera l'n-esimo valore del campo di intestazione
String getHeaderField(int n);
// restituisce uno stream per leggere dalla risorsa
InputStream getInputStream();
// restituisce uno stream per scrivere sulla risorsa
OutputStream getOutputStream();
```

### URLConnectionTest

```Java
public class URLConnectionTest {
    public static void main(String[] args) {
        try{
            URL url = new URL("http://www.w3.org");
            URLConnection connection = url.openConnection();
            connection.connect();

            //print header fields
            int n = 1;
            String key;
            while((key=connection.getHeaderField(n)) != null){
                String value = connection.getHeaderField(n);
                System.out.println(key+": "+value);
                n++;
            }

            //print convenience functions
            System.out.println("------------");
            System.out.println("getContentType: "+connection.getContentType());
            System.out.println("getContentLength: "+connection.getContentLength());
            System.out.println("getContentEncoding: "+connection.getContentEncoding());
            System.out.println("getDate: "+connection.getDate());
            System.out.println("getExpiration: "+connection.getExpiration());
            System.out.println("getLastModified: "+connection.getLastModified());
            System.out.println("------------");

            //print ten first lines of contents
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            n = 1;
            while((line=in.readLine()) != null && n <= 10){
                System.out.println(line);
                n++;
            }
            if(line != null){
                System.out.println("...");
            }
        }catch(IOException exception){
            System.out.println(exception.getMessage());
        }
    }
}
```