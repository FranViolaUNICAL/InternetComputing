package esempiUID;

public class Professore extends Persona{
    // private String nome; cognome;
    private String corso;

    // public String parla()...
    public Professore(String nome, String cognome, String corso) {
        super(nome, cognome);
        this.corso = corso;
    }

    public String spiega(){
        return parla();
    }

    @Override
    public String parla() {
        return "Sono un professore";
    }
}
