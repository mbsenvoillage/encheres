package fr.eni.encheres.bo;

import java.io.Serializable;
import java.time.LocalDate;

public class articleBean implements Serializable {

    // Les localdate sont castées en sql.date lors de l'insert
    private int artNb;
    private String artName;
    private String artDescrip;
    private LocalDate startAuc;
    private LocalDate endAuc;
    private int startPrice;
    private int salePrice;
    private PickUp pickUp;
    private Category category;
    private String saleStatus;

    public articleBean() {
    }

    public int getArtNb() {
        return artNb;
    }

    public void setArtNb(int artNb) {
        this.artNb = artNb;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtDescrip() {
        return artDescrip;
    }

    public void setArtDescrip(String artDescrip) {
        this.artDescrip = artDescrip;
    }

    public LocalDate getStartAuc() {
        return startAuc;
    }

    public void setStartAuc(LocalDate startAuc) {
        this.startAuc = startAuc;
    }

    public LocalDate getEndAuc() {
        return endAuc;
    }

    public void setEndAuc(LocalDate endAuc) {
        this.endAuc = endAuc;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public PickUp getPickUp() {
        return pickUp;
    }

    public void setPickUp(PickUp pickUp) {
        this.pickUp = pickUp;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }
}
