package betserver;

import java.net.InetAddress;
import java.util.Objects;

public class Scommessa {
    private int ID_scommessa;
    private int N_cavallo;
    private long puntata;
    private InetAddress scommettitore;
    private static int nextID = 0;

    public Scommessa(int n_cavallo, long punt, InetAddress scommettitore) {
        ID_scommessa = nextID++;
        N_cavallo = n_cavallo;
        puntata = punt;
        this.scommettitore = scommettitore;
    }

    public int getID_scommessa() {
        return ID_scommessa;
    }

    public void setID_scommessa(int ID_scommessa) {
        this.ID_scommessa = ID_scommessa;
    }

    public int getN_cavallo() {
        return N_cavallo;
    }

    public void setN_cavallo(int n_cavallo) {
        N_cavallo = n_cavallo;
    }

    public long getPuntata() {
        return puntata;
    }

    public void setPuntata(long puntata) {
        this.puntata = puntata;
    }

    public InetAddress getScommettitore() {
        return scommettitore;
    }

    public void setScommettitore(InetAddress scommettitore) {
        this.scommettitore = scommettitore;
    }

    public static int getNextID() {
        return nextID;
    }

    public static void setNextID(int nextID) {
        Scommessa.nextID = nextID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Scommessa scommessa = (Scommessa) o;
        return ID_scommessa == scommessa.ID_scommessa && N_cavallo == scommessa.N_cavallo && puntata == scommessa.puntata && Objects.equals(scommettitore, scommessa.scommettitore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID_scommessa, N_cavallo, puntata, scommettitore);
    }
}
