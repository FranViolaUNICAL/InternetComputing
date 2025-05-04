package tracce.concorso;

import java.io.Serializable;
import java.util.Objects;

public class Partecipazione implements Serializable {
    private String ID, nome, cognome, cf, curriculum;

    public Partecipazione(String ID, String nome, String cognome, String cf, String curriculum) {
        this.ID = ID;
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.curriculum = curriculum;
    }

    public String getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Partecipazione that = (Partecipazione) o;
        return Objects.equals(ID, that.ID) && Objects.equals(nome, that.nome) && Objects.equals(cognome, that.cognome) && Objects.equals(cf, that.cf) && Objects.equals(curriculum, that.curriculum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, nome, cognome, cf, curriculum);
    }
}
