package fr.eni.encheres.util;

import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.biddingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class sortResults {

    
    public static List<biddingBean> getBidsForOngoingAuc(List<biddingBean> bids) throws NullPointerException{
        List<biddingBean> selectedbids = new ArrayList<biddingBean>();


        bids.forEach((bid) -> {
            if (bid.getArtForSale().getSaleStatus().equals("EC")) {
                selectedbids.add(bid);
            }
        });

        return selectedbids;
    }

    public static List<biddingBean> getAuctionWins(List<biddingBean> bids) throws NullPointerException{
        List<biddingBean> selectedbids = new ArrayList<biddingBean>();


        bids.forEach((bid) -> {
            if (bid.getArtForSale().getSaleStatus().equals("RE")) {
                selectedbids.add(bid);
            }
        });

        return selectedbids;
    }

    public static List<articleBean> getUserNonStartedSales(List<articleBean> articles) throws NullPointerException {
        List<articleBean> selectedarticles = new ArrayList<articleBean>();

        articles.forEach((article) -> {
            if (article.getSaleStatus().equals("CR")) {
                selectedarticles.add(article);
            }
        });

        return selectedarticles;
    }

    public static List<articleBean> getUserFinishedSales(List<articleBean> articles) throws NullPointerException {
        List<articleBean> selectedarticles = new ArrayList<articleBean>();

        articles.forEach((article) -> {
            if (article.getSaleStatus().equals("ET")) {
                selectedarticles.add(article);
            }
        });
        return selectedarticles;

    }

    public static List<articleBean> filterArticlesByCat(List<articleBean> articles, String cat) {
        List<articleBean> selectedarticles = new ArrayList<articleBean>();

        articles.forEach((article) -> {
            if (article.getCategory().getCatName().equals(cat)) {
                selectedarticles.add(article);
            }
        });
        return selectedarticles;

    }

    public static List<biddingBean> filterBidsByCat(List<biddingBean> bids, String cat) {
        List<biddingBean> selectedbids = new ArrayList<biddingBean>();

        bids.forEach((bid) -> {
            if (bid.getArtForSale().getCategory().getCatName().equals(cat)) {
                selectedbids.add(bid);
            }
        });
        return selectedbids;
    }

    public static List<articleBean> filterArticlesByKeyword(List<articleBean> articles, String keyword) {
        List<articleBean> selectedarticles = new ArrayList<articleBean>();

        articles.forEach((article) -> {
            if (article.getArtName().contains(keyword)) {
                selectedarticles.add(article);
            }
        });
        return selectedarticles;
    }

    public static List<biddingBean> filterBidsByKeyword(List<biddingBean> articles, String keyword) {
        List<biddingBean> selectedbids = new ArrayList<biddingBean>();

        articles.forEach((bid) -> {
            if (bid.getArtForSale().getArtName().contains(keyword)) {
                selectedbids.add(bid);
            }
        });
        return selectedbids;
    }

}
