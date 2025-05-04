package tracce.concorso;

import java.util.Date;

public class Concorso {
    private String ID;
    private int posti;
    private Date data;

    public Concorso(String ID, int posti, Date data) {
        this.ID = ID;
        this.posti = posti;
        this.data = data;
    }

    public String getID() {
        return ID;
    }

    public int getPosti() {
        return posti;
    }

    public void setPosti(int posti) {
        this.posti = posti;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
