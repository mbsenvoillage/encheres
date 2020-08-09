package fr.eni.encheres.bo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class userBean implements Serializable {
    private int userNb;
    private String pseudo;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String rue;
    private String cpo;
    private String ville;
    private String mdp;
    private int credit;
    private boolean isAdmin;
    private boolean connecte;
    private Set<userAuth> userAuthTokens = new HashSet<>(0);

    //  Constructors

    public userBean() {};

    public userBean(String pseudo, String nom, String prenom, String email, String telephone, String rue, String cpo, String ville, String mdp) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.cpo = cpo;
        this.ville = ville;
        this.mdp = mdp;
    }

    // Getters and setters

    // USER
    public int getUserNb() {
        return userNb;
    }

    public void setUserNb(int userNb) {
        this.userNb = userNb;
    }

    // PSEUDO
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    // NOM
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // PRENOM

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    //EMAIL

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //TELEPHONE

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // RUE

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    // CPO

    public String getCpo() {
        return cpo;
    }

    public void setCpo(String cpo) {
        this.cpo = cpo;
    }

    // VILLE

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    // MDP

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    //CREDIT

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    // CONNECTE

    public boolean isConnecte() {
        return connecte;
    }

    public void setConnecte(boolean connecte) {
        this.connecte = connecte;
    }

    // ADMIN

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<userAuth> getUserAuthTokens() {
        return userAuthTokens;
    }

    public void setUserAuthTokens(Set<userAuth> userAuthTokens) {
        this.userAuthTokens = userAuthTokens;
    }
}
