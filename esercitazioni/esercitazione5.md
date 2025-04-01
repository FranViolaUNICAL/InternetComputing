# Esercitazione 5

Uno dei problemi principali nell'industria è quello di integrare applicazioni informatiche sviluppate in maniera indipendente, in presenza di gran numero di tecnologie eterogenee esistenti e proliferazione delle applicazioni distribuite.

La cosiddetta **integrazione applicativa** può essere considerata a diversi livelli:
1. All'interno della stessa azienda
2. Tra partner dell'azienda
3. Tra l'azienda ed utenti generici

L'integrazione è necessaria quando un processo coinvolge diversi sistemi informatici. Sfruttare internet come piattaforma globale di integrazione è una grande opportunità, soprattutto per l'integrazione tra diverse aziende. Essa è però resa difficile dalle politiche di sicurezza. 

## Web Service

Un Web Service è un'applicazione messa a disposizione (pubblicata) da una macchina ed accessibile attraverso protocolli standard di Internet (in genere http sulla porta 80 per evitare il blocco dei firewall).

Essi presentano le seguenti caratteristiche:
1. Interoperabilità: un WS può essere invocato da un client situato in una piattaforma tecnologica diversa da quella su cui è eseguito il servizio
2. Incapsulamento: gli utilizzatori di un WS sono ignari dei dettagli dell'implementazione, conoscono solo l'interfaccia
3. Accessibilità: un WS può essere reso pubblicamente disponibile per l'utilizzo

Gli standard utilizzati per i WS sono tutti dialetti di XML, come:

1. SOAP (Simple Object Access Protocol): descrive un protocollo basato su XML che definisce i meccanismi con cui un WS è invocato ed il formato dell'input e dell'output.
2. WSDL (Web Service Definition Language): descrive l'interfaccia esterna di un Web Service affinchè uno sviluppatore possa creare un client capace di invocarlo
3. UDDI (Universal Discovery, Description and Integration): è un registro contenente informazioni utili per la scoperta e l'accesso ai Web Services. 

La differenza fra Web e Web Services è che nel Web un browser richiede una pagina web in HTTP, mentre per i Web Services un client (es. programma Java) invoca un Web Service tramite SOAP e HTTP.

Essi sono una tecnica di Remote Procedure Call.

### Funzionamento di un Web Services
1. Il fornitore del servizio realizza il WS e lo pubblica in un registro UDDI.
2. Il client ricerca il file WSDL che descrive il WS nel registro UDDI
3. Il client recupera dall'UDDI il documento WSDL che definisce l'interfaccia del servizio
4. Il client costruisce il proxy per l'invocazione del servizio
5. Il client invoca il servizio e riceve la risposta

### XML Schema
Lo XML Schema è un linguaggio di descrizione del contenuto di un file XML, l'unico che finora abbia raggiunto la validazione ufficiale del W3C. 

Un vettore è rappresentato come una sequenza ordinata di elementi ripetuti dello stesso tipo di dati.

```XML
<xs:complexType name="ItemListType">
    <xs:sequence>
        <xs:element name="item" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
    </xs:sequence>
</xs:complexType>
```

1. L'indicatore sequence specifica che gli elementi figli devono apparire in un ordine specifico
2. L'indicatore <maxOccurs> specifica il numero massimo di volte in cui un elemento può verificarsi
3. L'indicatore <minOccurs> specifica il numero minimo di volte in cui un elemento può verificarsi

Un vettore è una sequenza ordinata di elementi ripetuti dello stesso tipo di dati. Questo è un costrutto molto comune nei linguaggi di programmazione che appaiono come una array o list. Alcuni strumenti che generano schemi da un modello possono scegliere di assegnare a ciascun vettore un tipo dedicato che include un elemento wrapper.

## Simple Object Access Protocol (SOAP)
E' uno standard W3C per l'invocazione di servizi attraverso XML.

Lo standard contiene:
1. La specifica dei messaggi di input e di output
2. Le regole di codifica per i tipi di dati

SOAP utilizza HTTP per la trasmissione dei dati, permette di oltrepassare i firewall ed è interpretato dal web server. 

### Struttura del SOAP

1. Elementi Esterni 
   1. Envelope
   2. Attachmetns
2. Elementi Interni
   1. Header: info su sicurezza, routing, formati, autenticazione, pagamenti, ecc...
   2. Body: contenuto vero e proprio del messaggio
      3. Fault Element: contiene errori ed informazioni di stato del messaggio SOAP

Le regole per realizzare un messaggio SOAP sono le seguenti:
1. Deve essere codificato in XML
2. Deve utilizzare il SOAP Envelope namespace: l'elemento envelope del messaggio è la radice (root) del messaggio SOAP e serve a definire che il messaggio XML è un messaggio costruito in accordo al protocollo SOAP. Il namespace definito all'interno di questo blocco definisce il modello da utilizzare per la costruzione di tale messaggio.
3. Deve utilizzare il SOAP Encoding namespace
4. Non deve contenere il collegamento ad un DTD (Document Type Definition) e non deve contenere istruzioni per processare XML. 

```XML
<soap:envelope>
    xmlns:soap="http://www.w3.org/2003/05/soap-envelope/"
    soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">
    ...
    <soap:Header>
        ...
    </soap:Header>
    <soap:Body>
        ...
        <soap:Fault>
            ...
        </soap:Fault>
    </soap:Body>
</soap:envelope>
```