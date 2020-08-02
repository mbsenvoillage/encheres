package fr.eni.encheres.bo;

public class Category {

    private int catNb;
    private String catName;

    // CONSTRUCTORS

    public Category() {};

    public Category(String catName) {
        this.catName = catName;
    }

    // GETTERS AND SETTERS

    public int getCatNb() {
        return catNb;
    }

    public void setCatNb(int catNb) {
        this.catNb = catNb;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}
