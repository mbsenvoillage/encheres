package fr.eni.encheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class biddingBean implements Serializable {

    private userBean seller;
    private LocalDateTime bidDate;
    private int bidAmount;
    private int buyerId;
    private String buyerName;

    // CONSTRUCTOR

    public biddingBean() {
        seller = new userBean();
    }

    // GETTERS AND SETTERS

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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}
