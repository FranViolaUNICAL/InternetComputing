package tracce.asta;

public class Offerta {
    private String cf;
    private int id_asta;
    private int somma;

    public Offerta(String cf, int id_asta, int somma) {
        this.cf = cf;
        this.id_asta = id_asta;
        this.somma = somma;
    }

    public String toString() {
        return cf + " " + id_asta + " " + somma;
    }
}
