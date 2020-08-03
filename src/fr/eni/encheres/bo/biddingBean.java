package fr.eni.encheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class biddingBean implements Serializable {

    private articleBean artForSale;
    private userBean seller;
    private LocalDateTime bidDate;
    private int bidAmount;
    private int buyerId;

    // CONSTRUCTOR

    public biddingBean() {
        artForSale = new articleBean();
        seller = new userBean();
    }

    // GETTERS AND SETTERS

    public articleBean getArtForSale() {
        return artForSale;
    }

    public void setArtForSale(articleBean artForSale) {
        this.artForSale = artForSale;
    }

    public userBean getSeller() {
        return seller;
    }

    public void setSeller(userBean seller) {
        this.seller = seller;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public LocalDateTime getBidDate() {
        return bidDate;
    }

    public void setBidDate(LocalDateTime bidDate) {
        this.bidDate = bidDate;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }
}
