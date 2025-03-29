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
