package tracce.monitoraggioPrezzi;

public class Offerta {
    String iva, nazione, codiceProdotto;
    int prezzo, quantita;

    public Offerta(String iva, String nazione, int prezzo, int quantita, String codiceProdotto) {
        this.iva = iva;
        this.nazione = nazione;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.codiceProdotto = codiceProdotto;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(String codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }


}
