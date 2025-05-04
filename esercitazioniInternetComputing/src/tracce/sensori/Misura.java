package tracce.sensori;

import java.io.Serializable;
import java.net.Socket;

public class Misura implements Serializable {
    private static int counter = 0;
    private final int ID_MISURA = counter++;
    private double misura;
    private long timestamp;
    private int idSensore;

    public Misura(double misura, int idSensore) {
        this.misura = misura;
        this.timestamp = System.currentTimeMillis();
        this.idSensore = idSensore;
    }

    public int getIDMisura() {
        return ID_MISURA;
    }

    public double getMisura() {
        return misura;
    }

    public void setMisura(double misura) {
        this.misura = misura;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getIdSensore() {
        return idSensore;
    }
}
