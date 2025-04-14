package tracce.asta;

public class Asta {
    private static int counter = 0;
    private final int ID;
    private String nome_prodotto;

    public Asta(String nome_prodotto) {
        ID = ++counter;
        this.nome_prodotto = nome_prodotto;
    }

    public int getID() {
        return ID;
    }

    public String getNome_prodotto() {
        return nome_prodotto;
    }

    public void setNome_prodotto(String nome_prodotto) {
        this.nome_prodotto = nome_prodotto;
    }

    public String toString() {
        return ID + " " + nome_prodotto;
    }
}
