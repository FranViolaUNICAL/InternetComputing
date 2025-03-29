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

