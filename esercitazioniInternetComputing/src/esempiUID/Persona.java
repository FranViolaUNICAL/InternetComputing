package esempiUID;

public class Persona {
    private String nome, cognome;

    public Persona(String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
    }

    public String parla(){
        return "Sto parlando";
    }
}
