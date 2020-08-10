package fr.eni.encheres.bo;

public class PickUp {

    private String rue;
    private String cpo;
    private String ville;
    private int artNb;

    // CONSTRUCTORS

    public PickUp() {};

    public PickUp(String rue, String cpo, String ville) {
        this.rue = rue;
        this.cpo = cpo;
        this.ville = ville;
    }

    // GETTERS AND SETTERS

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCpo() {
        return cpo;
    }

    public void setCpo(String cpo) {
        this.cpo = cpo;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String toString() {
        return rue + "\n" + "\t" + cpo + " " + ville;
    }

    public int getArtNb() {
        return artNb;
    }

    public void setArtNb(int artNb) {
        this.artNb = artNb;
    }
}
