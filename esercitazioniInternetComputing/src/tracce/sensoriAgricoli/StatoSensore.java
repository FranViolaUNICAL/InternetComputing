package tracce.sensoriAgricoli;

public class StatoSensore {
    private static int counter = 0;
    private final int ID_SENSORE;
    private int stato = 0;
    private double temp;
    private double hum;

    public int getID_SENSORE() {
        return ID_SENSORE;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    public String toString(){
        return ID_SENSORE + " " + stato + " " + temp + " " + hum;
    }
}
