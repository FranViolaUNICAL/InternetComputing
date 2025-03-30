# Esercitazione 3.02
## Nat (Richiamo)
Il NAT (Network Access Translation) é una tecnica che consiste nel modificare gli indirizzi IP contenuti negli header dei pacchetti in transito su un sistema che agisce da router all'interno di una comunicazione tra due o piú host.

Il NAT é spesso implementato dai router e dai firewall. Si é affermato come mezzo per ovviare la scarsitá di indirizzi IP pubblici disponibili.

Nelle reti domestiche, solitamente agli host della rete privata viene assegnato un indirizzo IP interno dal router (es. 192.168.0.10). Il router acquisisce un IP pubblico dal server di destinazione (es. ISP) e i singoli host si presentano all'esterno con l'IP acquisito dal router. Si parla di IP Masquerading o NAT dinamico.

In presenza di IP Masquerading puó essere necessario che alcuni host o servizi di rete ospitati sulla rete "mascherata" siano accessibili dall'esterno. La soluzione é detta Port Forwarding, per cui le connessioni verso una determianta porta TCP o UDP dell'indirizzo esterno vengono reindirizzate verso un particolare host della rete interna.

## Accesso PC Remoto con NAT Dinamico
Per accedere ad un computer della rete interna in presenza di IP masquerading o NAT dinamico é necessario configurare delle regole redirect delle chiamate sul router.

Ogni regola é caratterizzata da:
1. una porta
2. un protocollo
3. un host destinazione

Le chiamate inviate usando l'IP pubblico del router, la porta ed il protocollo di una particolare regola, vengono automaticamente reindirizzate dal router all'host destinazione.

Per scoprire l'IP pubblico del mio router si puó usare il pannello di controllo del router oppure usare servizi web come whatismyip.com. Gli ISP assegnano al router un IP dinamico, ovvero un IP che puó cambiare col tempo. Solo in casi particolari, previa pagamento, é possibile assegnare ad un router un IP statico.

In caso di IP dinamici potrebbe diventare poco pratico contattare il router. Per ovviare a questo problema di fa uso del DNS dinamico, ovvero un servizio attraverso il quale é possibile identificare una connessione pubblica dinamica tramite nome host. Il router viene configurato in modo tale che ogni volta in cui il suo Indirizzo IP pubblico cambia, esso comunica al servizio di DNS Dinamico il nuovo IP. É possibile effettuare una connessione immediata al router usando il nome host registrato sul servizio di DNS dinamico.

Esistono diversi servizi di DNS dinamico: DynDNS.com, No-IP.com, DtDNS.com. Questi servizi solitamente sono gratuiti, dietro a delle limitazioni:
1. numero host name consentiti per account
2. numero domini
3. necessitá di confermare il dominio ogni 30g

La maggior parte dei router in commercio sono giá predisposti per configurare il DNS dinamico e supportano i principali service provider in commercio.

## Firewall
La presenza di un firewall sulla vostra connessione potrebbe impedire connessioni remote agli host della rete. É necessario configurare il firewall in modo tale da consentire comunicazioni in ingresso/uscita su determinate porte del sistema e/o con particolari host esterni alla rete LAN.

Configurazioni a livello dell'OS potrebbero rendersi necessarie anche nel caso in cui si voglia utilizzare software per il controllo remoto delle macchine (es. TinyVNC o RealVNC).