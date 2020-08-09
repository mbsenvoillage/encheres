package fr.eni.encheres.bo;

import java.io.Serializable;

public class userAuth implements Serializable {
    private int id;
    private String selector;
    private String validator;
    private userBean user;

    public userAuth() {
        user = new userBean();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public userBean getUser() {
        return user;
    }

    public void setUser(userBean user) {
        this.user = user;
    }
}
