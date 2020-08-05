package fr.eni.encheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class biddingBean implements Serializable {

    private LocalDateTime bidDate;
    private int bidAmount;
    private int buyerId;
    private String buyerName;
    private int artNb;

    // CONSTRUCTOR

    public biddingBean() { }

    public biddingBean(int bidamount, LocalDateTime date, int artNb, int buyerId) {
        this.bidAmount = bidamount;
        this.bidDate = date;
        this.artNb = artNb;
        this.buyerId = buyerId;
    }

    // GETTERS AND SETTERS

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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getArtNb() {
        return artNb;
    }

    public void setArtNb(int artNb) {
        this.artNb = artNb;
    }
}
