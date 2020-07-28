package fr.eni.encheres.bo;

import java.io.Serializable;

public class loginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean connecte;

    public loginBean() {};

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnecte() {
        return connecte;
    }

    public void setConnecte(boolean connecte) {
        this.connecte = connecte;
    }
}
