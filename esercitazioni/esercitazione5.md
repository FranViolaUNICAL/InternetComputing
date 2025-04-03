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

### Esempio: SOAP Request/Response
```HTML
POST /InStock HTTP/1.1
Host: www.example.org
Content-Type: application/soap+xml;
charset=utf-8
Content-Length: nnn
```
```XML
<?xml version="1.0"?>
<soap:Envelope
xmlsn:soap="http://www.w3.org/2003/05/soap-envelope/"
soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">

<soap:Body
xmlns:m="http://www.example.org/stock">
    <m:GetStockPrice>
       <m:StockName>IBM</m:StockName>
    </m:GetStockPrice>
</soap:Body>
   
</soap:Envelope>
```

```HTTP
HTTP/1.1 200 OK
Content-Type:application/soap+xml;
charset=utf-8
Content-Length: nnn
```

```XML
<?xml version="1.0"?>
<soap:Envelope
xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
soap:encodingStyle="http://www.w3.org/2003/05/soap-encoding">
   <soap:Body xmlns:m="http://www.example.org/stock">
      <m:GetStockPriceResponse>
         <m:Price>34.5</m:Price>
      </m:GetStockPriceResponse>
   </soap:Body>
   
</soap:Envelope>
```

## Web Service Description Language (WSDL)

Lo standard WSDL, acronimo di Web Service Description Language è lo standard W3C per la descrizione in XML dell'interfaccia, ovvero delle API, del web. 

Nel file WSDL è ovviamente riportato anche l'endpoint, ovvero la location dove si trova il servizio. Un file WSDL è un descrittore, quindi, dell'intero Web Service.

Un client che ha a disposizione il file WSDL di un servizio, conosce quindi tutte le informazioni necessarie per usare quel servizio.

### Struttura del documento WSDL

I documenti WSDl devono essere resi disponibili su registri UDDI. Essi descrivono:
1. Cosa un WS può fare
2. Dove risiede
3. Come invocarlo

E' costituito essenzialmente da 5 elementi XML:
1. Types: i tipi di dato che possono essere scambiati tra client e web service
2. Messages: i messaggi che possono essere scambiati tra web service e client, sono definiti come composizione o aggregazione dei tipi elementari
3. Port Types: definisce i punti di connessione (operazioni) verso il WebService (ogni operazione esposta ha un elemento portType); è un interfaccia che contiene la definizione di operazioni aventi messaggi di input e output.
4. Bindings: fornisce dettagli implementativi per il tipo di porta, informazioni su come realizzare (implementare) la porta ed in particoare sul metodo di trasporto (soap, http, smtp);
5. Services: indica dove le porte sono fisicamente realizzate (deployed); fornisce una descrizione testuale di ciascuna porta (leggibile dall'uomo) e informa i client da dove accedere.

#### Esempi di Types
```XML
<types>
   <schema targetNamespace = "http://example.com/stockquote.xsd"
           xmlns = "http://www.w3.org/2000/10/XLMSchema">
      <element name = "TradePriceRequest">
         <complexType>
            <all>
               <element name = "tickerSymbol" type = "string"/>
            </all>
         </complexType>
      </element>
      <element name="TradePrice">
         <complexType>
            <all>
               <element name = "price" type = "float" />
            </all>
         </complexType>
      </element>
   </schema>
</types>
```

### Esempio di Documento WSDL
```XML
<?xml version="1.0" encoding="UTF-8" ?>
<definitions name="HelloService"
    targetNamespace="http://www.ecerami.com/wsdl/HelloService.wsdl"
    xmlns="http://schemas.xmlsoap.org/wsdl/"
    ...
    <message>
        <part name="firstName" type="xsd:string"/>
    </message>
    ...
    <portType name="Hello_PortType">
        <operation name="sayHello">
           <input message="tns:SayHelloRequest"/>
           <output message="tns:SayHelloResponse"/>
        </operation>
    </portType>
    <binding name="Hello_Binding" type="tns:Hello_PortType">
        <soap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http"/>
        <operation name="sayHello">
           ...
        </operation>
    </binding>
    <service name="HelloService">
        ...
    </service>
</definitions>
```

In questo caso si noti come non si usano types particolari quindi avremo solo tipi di base. 

L'elemento operation in portType ci permette di combinare request e response in un'unica operazione. Un portType può definire più operazioni. E' un'astrazione, i dettagli li fornisce il <binding>.

L'elemento binding fornisce i dettagli implementativi di un portType

L'elemento service fornisce le definizioni di ogni servizio. Per ciascuno dei protocolli supportati, esiste un elemento di <port>. L'elemento di servizio è una raccolta di porte. 

Per ciascuno dei protocolli supportati, esiste un elemento <port> in <service>. L'elemento service è quindi una raccolta di port. I client possono leggere questa sezione per capire:
1. Dove accedere al servizio (location)
2. In accordo a quale port type (elemento binding di <port>) e, quindi, quali messaggi di comunicazione 
3. <documentation> include anche una documentazione human-readable per il servizio

```XML
<service name="Hello_Service">
   <documentation>WSDL File for HelloService</documentation>
   <port binding ="tns:Hello_Binding" name = "Hello_Port">
      <soap:address
         location="http://www.examples.com/SayHello/"/>
   </port>
</service>
```

## UDDI (Universal Description Discovery Integration)
E' uno standard per distribuire e reperire i Web Services. Un registro UDDI contiene i documenti WSDL associati ad un insieme di servizi, ed informazioni addizionali riguardanti le credenziali d'accesso richieste dai servizi.

L'accesso ai registri UDDI può essere:
1. Pubblico
2. Privato
3. Ibrido

### Esempio1-WS

#### Classe Java che implementa il servizio

```Java
public class Esempio1{
    public String saluto(){
        return "Ciao Mondo";
    }
}
```

#### Documento WSDL
```XML
<wsdl:definitions targetNamespace="http://localhost:8080/axis/services/Esempio1">
   <wsdl:message name="salutoRequest"> </wsdl:message>
   <wsdl:message name="salutoResponse">
      <wsdl:part name="salutoReturn" type="xsd:string"/>
   </wsdl:message>
   <wsdl:portType name="Esempio1">
      <wsdl:operation name="saluto">
         <wsdl:input message="impl:salutoRequest" name="salutoRequest"/>
         <wsdl:output message="impl:salutoResponse" name="salutoResponse"/>
      </wsdl:operation>
   </wsdl:portType>
   <wsdl:binding name="Esempio1SoapBinding" type="impl:Esempio1">
      <wsdlsoap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http"/>
      <wsdl:operation name="saluto">
         <wsdlsoap:operation soapAction=""/>
         <wsdl:input name="salutoRequest">
            <wsdlsoap:body encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" namespace="http://DefaultNamespace" use="encoded"/>
         </wsdl:input>
         <wsdl:output name="salutoResponse">
            <wsdlsoap:body encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" namespace="http://localhost:8080/axis/services/Esempio1" use="encoded"/>
         </wsdl:output>
      </wsdl:operation>
   </wsdl:binding>
   <wsdl:service name="Esempio1Service">
      <wsdl:port binding="impl:Esempio1SoapBinding" name="Esempio1">
         <wsdlsoap:address location="http://localhost:8080/axis/services/Esempio1"/>
      </wsdl:port>
   </wsdl:service>
</wsdl:definitions>
```

### Esempio2-WS

#### File Java
```Java
public class Esempio2{
    public String saluto(String nome){
        return "Ciao " + nome;
    }
}
```

#### Documento WSDL
```XML
<wsdl:definitions targetNamespace="http://localhost:8080/axis/services/Esempio2">
   <wsdl:message name="salutoRequest">
      <wsdl:part name="in0" type="soapenc:string"/>
   </wsdl:message>
   <wsdl:message name="salutoResponse">
      <wsdl:part name="salutoReturn" type="xsd:string"/>
   </wsdl:message>
   <wsdl:portType name="Esempio2">
      <wsdl:operation name="saluto" parameterOrder="in0">
         <wsdl:input message="impl:salutoRequest" name="salutoRequest"/>
         <wsdl:output message="impl:salutoResponse" name="salutoResponse"/>
      </wsdl:operation>
   </wsdl:portType>
   <wsdl:binding name="Esempio2SoapBinding" type="impl:Esempio2">
      <wsdlsoap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http"/>
      <wsdl:operation name="saluto">
         <wsdlsoap:operation soapAction=""/>
         <wsdl:input name="salutoRequest">
            <wsdlsoap:body encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" namespace="http://DefaultNamespace" use="encoded"/>
         </wsdl:input>
         <wsdl:output name="salutoResponse">
            <wsdlsoap:body encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" namespace="http://localhost:8080/axis/services/Esempio2" use="encoded"/>
         </wsdl:output>
      </wsdl:operation>
   </wsdl:binding>
   <wsdl:service name="Esempio2Service">
      <wsdl:port binding="impl:Esempio2SoapBinding" name="Esempio2">
         <wsdlsoap:address location="http://localhost:8080/axis/services/Esempio2"/>
      </wsdl:port>
   </wsdl:service>
</wsdl:definitions>
```

### Esempio3-WS

#### Classe Java
```Java
public class Esempio3{
    public String saluto(String nome, String cognome){
        return "Ciao " + nome + " " + cognome;
    }
}
```

#### Documento WSDL
```XML
<wsdl:definitions targetNamespace="http://localhost:8080/axis/services/Esempio3">
   <wsdl:message name="salutoRequest">
      <wsdl:part name="in0" type="soapenc:string"/>
      <wsdl:part name="in1" type="soapenc:string"/>
   </wsdl:message>
   <wsdl:message name="salutoResponse">
      <wsdl:part name="salutoReturn" type="xsd:string"/>
   </wsdl:message>
   <wsdl:portType name="Esempio3">
      <wsdl:operation name="saluto" parameterOrder="in0 in1">
         <wsdl:input message="impl:salutoRequest" name="salutoRequest"/>
         <wsdl:output message="impl:salutoResponse" name="salutoResponse"/>
      </wsdl:operation>
   </wsdl:portType>
   <wsdl:binding name="Esempio3SoapBinding" type="impl:Esempio3">
      <wsdlsoap:binding style="rpc" transport="http://schemas.xmlsoap.org/soap/http"/>
      <wsdl:operation name="saluto">
         <wsdlsoap:operation soapAction=""/>
         <wsdl:input name="salutoRequest">
            <wsdlsoap:body encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" namespace="http://DefaultNamespace" use="encoded"/>
         </wsdl:input>
         <wsdl:output name="salutoResponse">
            <wsdlsoap:body encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" namespace="http://localhost:8080/axis/services/Esempio3" use="encoded"/>
         </wsdl:output>
      </wsdl:operation>
   </wsdl:binding>
   <wsdl:service name="Esempio3Service">
      <wsdl:port binding="impl:Esempio3SoapBinding" name="Esempio3">
         <wsdlsoap:address location="http://localhost:8080/axis/services/Esempio3"/>
      </wsdl:port>
   </wsdl:service>
</wsdl:definitions>
```

### Esempio4-WS
#### Classe Java
```Java
public class Esempio4{
    public String saluto(String[] nomi){
        String s = "Ciao";
        for(int i = 0; i < nomi.length, i++){
            s += " " + nomi[i];
            if(i < nomi.length - 1){
                s += " ";
            }
       }
        return s;
    }
}
```

#### Documento WSDL
Il documento è sostanzialmente uguale a quello visto per l'esempio3, con l'aggiunta di una sezione \<wsdl:types\>

```XML
...
<wsdl:types>
   <schema targetNamespace="http://localhost:8080/axis/services/Esempio4">
      <import namespace="http://schemas.xmlsoap.org/soap/encoding"/>
      <complexType name="ArrayOf_xsd_string">
         <complexContent>
            <restriction base="soapenc:Array">
               <attribute ref="soapenc:arrayType" wsdl:arrayType="xsd:string[]"/>
            </restriction>
         </complexContent>
      </complexType>
   </schema>
</wsdl:types>
...
```

### Tag Types e Messages
Types descrive particolari XML data types usati nei messagi ed è opzionale.
Message definisce in modo astratto  i messaggi che devono essere scambiati:
1. Ogni metodo nell'interfaccia contiene 0-1 messaggi di richiesta e 0-1 messaggi di risposta
2. Ogni messaggio consiste di elementi part, in genere un'entità "part" per ogni variabile inviata o ricevuta. Le entità "part" possono essere o tipi primitivi XML oppure opportuni complex types.

### Tipi Base, String ed Array
int, double,...,String sono XML Schema primitive types e non necessitano di particolari definizioni nel file WSDL.
Gli array non sono primitive types: essi sono definiti nel SOAP encoding schema, quindi è sufficiente importare la definizione di questo XML schema.

### Il file WSDL
Esso è particolarmente verboso. Solitamente viene scritto in modo automatico da applicazioni come Apache Axis. 