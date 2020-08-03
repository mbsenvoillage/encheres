package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.biddingBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDAOJdbcImpl implements BidDAO {

    private final String SELECT_BIDS_BY_BUYER = "select a.no_article, a.nom_article, a.description, j.pseudo as 'seller' ,e.montant_enchere, a.prix_initial, e.date_enchere, a.date_fin_encheres from ARTICLES_VENDUS a\n" +
            "inner join ENCHERES E on a.no_article = E.no_article\n" +
            "inner join UTILISATEURS U on e.no_utilisateur = U.no_utilisateur\n" +
            "inner join UTILISATEURS J on a.no_utilisateur = j.no_utilisateur\n" +
            "where e.no_utilisateur = ? and a.etat_vente = 'EC'";

    public List<biddingBean> selectMyOnGoingBids(int buyerId) throws BusinessException {


        List<biddingBean> bids = new ArrayList<biddingBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_BIDS_BY_BUYER);

            stmt.setInt(1, buyerId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bids.add(bidBuilder(rs));
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return bids;

    }

    private biddingBean bidBuilder(ResultSet rs) throws SQLException {
        biddingBean bid = new biddingBean();

        bid.getArtForSale().setArtNb(rs.getInt("no_article"));
        bid.getArtForSale().setArtName(rs.getString("nom_article"));
        bid.getArtForSale().setArtDescrip(rs.getString("description"));
        bid.getSeller().setPseudo(rs.getString("seller"));
        bid.setBidAmount(rs.getInt("montant_enchere"));
        bid.getArtForSale().setStartPrice(rs.getInt("prix_initial"));
        bid.setBidDate(rs.getTimestamp("date_enchere").toLocalDateTime());
        bid.getArtForSale().setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());

        return bid;
    }

}
