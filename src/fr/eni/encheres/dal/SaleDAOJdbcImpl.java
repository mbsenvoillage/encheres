package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.*;
import fr.eni.encheres.util.SqlStatements;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAOJdbcImpl implements SaleDAO {



    // PERMET DE SÉLECTIONNER LES ENCHERES REMPORTEES PAR L'UTILISATEUR


    public List<articleBean> selectUserWinningBids(Integer userNb, String name, String cat) throws BusinessException {
        StringBuilder sqlstmt = new StringBuilder();
        sqlstmt.append(SqlStatements.SELECT_WINNING_BIDS);
        boolean skip = false;

        // Vérifie les paramètres pour ajouter des conditions à la requête

        if (!name.isEmpty()) {
            if (!cat.isEmpty()) {
                sqlstmt.append(SqlStatements.byNameAndCat);
            } else {
                sqlstmt.append(SqlStatements.byName);
            }
        } else if (!cat.isEmpty()) {
            sqlstmt.append(SqlStatements.byCat);
        }


        System.out.println(sqlstmt);
        List<articleBean> allArticles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));
            stmt.setInt(1, userNb);

            if (!name.isEmpty()) {
                if (!cat.isEmpty()) {
                    int no_cat = this.selectCatByName(cat);
                    stmt.setString(2, name);
                    stmt.setInt(3, no_cat);
                } else {
                    stmt.setString(2, name);
                }
            } else if (!cat.isEmpty()) {
                int no_cat = this.selectCatByName(cat);
                stmt.setInt(2, no_cat);
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                allArticles.add(articleBuilder(rs));
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return allArticles;
    }

    // PERMET DE METTRE A JOUR L'ETAT DE VENTE A CHAQUE REQUETE SELECT PROVENANT DE LA PAGE D'ACCUEIL

    public void updateSaleStatus() throws BusinessException {
        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SET_STATUS_TO_EC);
                stmt.executeUpdate();

                stmt = cnx.prepareStatement(SqlStatements.SET_STATUS_TO_ET);
                stmt.executeUpdate();

                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

            System.out.println("Status is being updated");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_UPDATE_DB);
            throw bizEx;
        }
    }

    public biddingBean selectHighestBidder(int artNb) throws BusinessException {
        biddingBean bid = new biddingBean();

        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SELECT_HIGHEST_BIDDER);
            System.out.println(SqlStatements.SELECT_HIGHEST_BIDDER);
            stmt.setInt(1, artNb);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bid.setBidAmount(rs.getInt("highest bid"));
                bid.setBuyerId(rs.getInt("no_utilisateur"));
                bid.setBuyerName(rs.getString("buyer"));
                bid.setBidDate(rs.getTimestamp("date_enchere").toLocalDateTime());
            }

        } catch (SQLException throwables) {
            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }
        return bid;
    }


    public articleBean detailAuction(int artNb) throws BusinessException {
        articleBean article = new articleBean();
        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SELECT_AUCTION_DETAIL);
            System.out.println(SqlStatements.SELECT_AUCTION_DETAIL);
            stmt.setInt(1, artNb);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                article = articleBuilder(rs);
            }

        } catch (SQLException throwables) {
            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }
        System.out.println(article.toString());
        return article;
    }

    public int checkUserCredit(int userNb) throws BusinessException {
        int credit = 0;
        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.CHECK_USER_CREDIT);
            stmt.setInt(1, userNb);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                credit = rs.getInt("credit");
            }

        } catch (SQLException throwables) {
            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }
        return credit;
    }

    public void updateArtSalePrice(int price, int artNb) throws BusinessException {
        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.UPDATE_ARTICLE_SALE_PRICE);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, price);
                stmt.setInt(2, artNb);

                // Envoie la requête
                stmt.executeUpdate();

                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_UPDATE_DB);
            throw bizEx;
        }

    }

    public biddingBean insertBid(biddingBean bid) throws BusinessException {
        // Si la méthode hérite d'un objet vide une erreur est levée
        if (bid == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.INSERT_NEW_BID);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, bid.getBuyerId());
                stmt.setInt(2, bid.getArtNb());
                stmt.setTimestamp(3, Timestamp.valueOf(bid.getBidDate()));
                stmt.setInt(4, bid.getBidAmount());

                // Envoie la requête
                stmt.executeUpdate();

                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_INSERT_OBJECT);
            throw bizEx;
        }


        return bid;
    }

    public List<articleBean> selectUserItemsForSale(Integer userNb, String name, String cat, String status) throws BusinessException {
        StringBuilder sqlstmt = new StringBuilder();
        sqlstmt.append(SqlStatements.SELECT_ALL_ARTICLES);
        boolean skip = false;

        // Vérifie les paramètres pour ajouter des conditions à la requête

        if (!name.isEmpty()) {
            if (!cat.isEmpty()) {
                sqlstmt.append(SqlStatements.byNameAndCat);
            } else {
                sqlstmt.append(SqlStatements.byName);
            }
        } else if (!cat.isEmpty()) {
            sqlstmt.append(SqlStatements.byCat);
        } else {
            sqlstmt.append(" where etat_vente = ").append("'").append(status).append("'");
            skip = true;
        }

        if (!status.isEmpty() && !skip) {
            if (status.equals("EC")) {
                sqlstmt.append(SqlStatements.EC);
            } else if (status.equals("CR")) {
                sqlstmt.append(SqlStatements.CR);
            } else {
                sqlstmt.append(SqlStatements.ET);
            }
        }

        sqlstmt.append(" and a.no_utilisateur = ?");
        System.out.println(sqlstmt);
        List<articleBean> allArticles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));

            if (!name.isEmpty()) {
                if (!cat.isEmpty()) {
                    int no_cat = this.selectCatByName(cat);
                    stmt.setString(1, name);
                    stmt.setInt(2, no_cat);
                    stmt.setInt(3, userNb);
                } else {
                    stmt.setString(1, name);
                    stmt.setInt(2, userNb);
                }
            } else if (!cat.isEmpty()) {
                int no_cat = this.selectCatByName(cat);
                stmt.setInt(1, no_cat);
                stmt.setInt(2, userNb);
            } else
            {
                stmt.setInt(1, userNb);
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                allArticles.add(articleBuilder(rs));
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return allArticles;
    }


    // SELECTIONNE LES ENCHERES SELON LA CATEGORIE, LE MOT CLE, L'ETAT DE VENTE


    public List<articleBean> selectOngoingAuctions(int userNb, String name, String cat, String status) throws BusinessException {
        StringBuilder sqlstmt = new StringBuilder();
        sqlstmt.append(SqlStatements.SELECT_ONGOING_AUCS);
        boolean skip = false;


        // On ajoute à la requête les conditions selon les critères de recherche passés en paramètre de la requête HTTP
        // Ici le mot de recherche et la catégorie
        if (!name.isEmpty()) {
            if (!cat.isEmpty()) {
                sqlstmt.append(SqlStatements.byNameAndCat);
            } else {
                sqlstmt.append(SqlStatements.byName);
            }
        } else if (!cat.isEmpty()) {
            sqlstmt.append(SqlStatements.byCat);
        } else {
            sqlstmt.append(" where AV.etat_vente in ('EC', 'ET')");
            skip = true;
        }

        // Ici le statue de la vente
        if (!skip) {
        sqlstmt.append(" where AV.etat_vente in ('EC', 'ET')");
        }

        sqlstmt.append(" and A.no_utilisateur = ?");
        System.out.println("Le no utilisateur = " + userNb);


        // On ajoute les colonnes du GROUP BY

        sqlstmt.append("\ngroup by A.no_article, A.no_utilisateur, AV.nom_article, AV.description, A.max, U.pseudo, AV.date_fin_encheres, AV.prix_initial");

        List<articleBean> allArticles = new ArrayList<articleBean>();
        System.out.println(sqlstmt);


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));

            // Execution de la requête pour les utilisateurs ayant un compte

            // S'il y a un mot de recherche
            if (!name.isEmpty())
            {
                // Et s'il y a aussi une catégorie
                if (!cat.isEmpty())
                {
                    int no_cat = this.selectCatByName(cat);
                    stmt.setString(1, name);
                    stmt.setInt(2, no_cat);
                    stmt.setInt(3, userNb);
                    // s'il n'y a pas de caté et que le mot de recherche
                } else {
                    stmt.setString(1, name);
                    stmt.setInt(2, userNb);
                }
                // s'il n'y a pas de mot de recherche
            }
            else if (!cat.isEmpty())
            {
                int no_cat = this.selectCatByName(cat);
                stmt.setInt(1, no_cat);
                stmt.setInt(2, userNb);
            }
            else
            {
                stmt.setInt(1, userNb);
            }


            ResultSet rs = stmt.executeQuery();


            while(rs.next()) {
                allArticles.add(articleBuilder(rs));
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return allArticles;
    }


    //SELECTIONNE LES ARTICLES SELON MOT CLE, CATEGORIE

    public List<articleBean> selectAllArticles(String name, String cat, String status) throws BusinessException {
        StringBuilder sqlstmt = new StringBuilder();
        sqlstmt.append(SqlStatements.SELECT_ALL_ARTICLES);
        boolean skip = false;

        // Vérifie les paramètres pour ajouter des conditions à la requête

        if (!name.isEmpty()) {
            if (!cat.isEmpty()) {
                sqlstmt.append(SqlStatements.byNameAndCat);
            } else {
                sqlstmt.append(SqlStatements.byName);
            }
        } else if (!cat.isEmpty()) {
            sqlstmt.append(SqlStatements.byCat);
        } else {
            sqlstmt.append(" where etat_vente = ").append("'").append(status).append("'");
            skip = true;
        }

        if (!status.isEmpty() && !skip) {
            if (status.equals("EC")) {
                sqlstmt.append(SqlStatements.EC);
            } else if (status.equals("CR")) {
                sqlstmt.append(SqlStatements.CR);
            } else {
                sqlstmt.append(SqlStatements.ET);
            }
        }
        System.out.println(sqlstmt);
        List<articleBean> allArticles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));

            if (!name.isEmpty()) {
                if (!cat.isEmpty()) {
                    int no_cat = this.selectCatByName(cat);
                    stmt.setString(1, name);
                    stmt.setInt(2, no_cat);
                } else {
                    stmt.setString(1, name);
                }
            } else if (!cat.isEmpty()) {
                int no_cat = this.selectCatByName(cat);
                stmt.setInt(1, no_cat);
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                allArticles.add(articleBuilder(rs));
            }

            stmt.close();

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return allArticles;
    }

    // INSERE LES DONNES DE RETRAIT CORRESPONDANTES AU DEPOS D'UNE ANNONCE

    public void insertRetrait(PickUp retrait, int artNb) throws BusinessException {
        // Si la méthode hérite d'un objet vide une erreur est levée
        if (retrait == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }


        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.INSERT_PICKUP_DETAIL);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, artNb);
                stmt.setString(2, retrait.getRue());
                stmt.setString(3, retrait.getCpo());
                stmt.setString(4, retrait.getVille());

                // Envoie la requête
                stmt.executeUpdate();

                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_INSERT_OBJECT);
            throw bizEx;
        }
    }

    // PERMET DE MODIFIER LE LIEU DE RETRAIT D'UN ARTICLE

    public void updateRetrait(PickUp retrait, int artNb) throws BusinessException {
        // Si la méthode hérite d'un objet vide une erreur est levée
        if (retrait == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }


        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.UPDATE_PICKUP_DETAIL);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, artNb);
                stmt.setString(2, retrait.getRue());
                stmt.setString(3, retrait.getCpo());
                stmt.setString(4, retrait.getVille());
                stmt.setInt(5, retrait.getArtNb());

                // Envoie la requête
                stmt.executeUpdate();

                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_INSERT_OBJECT);
            throw bizEx;
        }
    }


    // PERMET D'INSÉRER UN ARTICLE DANS LA BASE DE DONNÉES

    public articleBean inserArticle(articleBean article, userBean user) throws BusinessException {

        // Si la méthode hérite d'un objet vide une erreur est levée
        if (article == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // On détermine le statut de la vente

        String status = setAucStatus(article.getStartAuc(), article.getEndAuc());

        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.INSERT_NEW_ARTICLE, PreparedStatement.RETURN_GENERATED_KEYS);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setString(1, article.getArtName());
                stmt.setString(2, article.getArtDescrip());
                stmt.setTimestamp(3, Timestamp.valueOf(article.getStartAuc()));
                stmt.setTimestamp(4, Timestamp.valueOf(article.getEndAuc()));
                stmt.setInt(5, article.getStartPrice());
                stmt.setInt(6, article.getStartPrice());
                stmt.setInt(7, user.getUserNb());
                stmt.setInt(8, article.getCategory().getCatNb());
                stmt.setString(9, status);

                // Envoie la requête
                stmt.executeUpdate();

                // Déclare un variable pour stocker le no_utilisateur
                ResultSet rs = stmt.getGeneratedKeys();

                // Si une clé a été assignée par la DB, on l'enregistre dans le userBean
                if (rs.next()) {
                    article.setArtNb(rs.getInt(1));
                }
                rs.close();
                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_INSERT_OBJECT);
            throw bizEx;
        }

        // Si l'insertion a fonctionné, l'utilisateur est connecté
        article.setSaleStatus(status);
        return article;
    }

    // PERMT DE MODIFIER UN ARTICLE EN DONT LA VENTE N'A PAS DÉBUTÉ


    public articleBean updateArticle(articleBean article, userBean user) throws BusinessException {

        // Si la méthode hérite d'un objet vide une erreur est levée
        if (article == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // On détermine le statut de la vente

        String status = setAucStatus(article.getStartAuc(), article.getEndAuc());

        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.UPDATE_ARTICLE);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setString(1, article.getArtName());
                stmt.setString(2, article.getArtDescrip());
                stmt.setTimestamp(3, Timestamp.valueOf(article.getStartAuc()));
                stmt.setTimestamp(4, Timestamp.valueOf(article.getEndAuc()));
                stmt.setInt(5, article.getStartPrice());
                stmt.setInt(6, article.getStartPrice());
                stmt.setInt(7, article.getCategory().getCatNb());
                stmt.setString(8, status);
                stmt.setInt(9, article.getArtNb());

                stmt.close();
                cnx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                cnx.rollback();
                throw e;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_UPDATE_DB);
            throw bizEx;
        }


        article.setSaleStatus(status);
        return article;
    }

    // PERMET DE DETERMINER LE NO D'UNE CATEGORIE A PARTIR DE SON NOM

    public int selectCatByName(String cat) throws BusinessException {

        int catId = 0;

        // Si la méthode hérite d'un objet vide une erreur est levée
        if(cat == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SELECT_CAT_BY_NAME);

            // Remplit les placeholders avec le username
            stmt.setString(1, cat);
            ResultSet rs = stmt.executeQuery();

            // Assigne au booléen ok, la valeur rs.next() qui parcours le resultset

            if(rs.next()) {
                catId = rs.getInt("no_categorie");
            }

            stmt.close();

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }
        return catId;
    }

    // PERMET DE DÉTERMINER LE STATUT DE VENTE


    private String setAucStatus(LocalDateTime start, LocalDateTime end) {
        String status = "";
        if (start.isAfter(LocalDateTime.now())) {
            status = "CR";
        }
        if (start.isBefore(LocalDateTime.now()) || start.isEqual(LocalDateTime.now())) {
            status = "EC";
        }
        if (end.isEqual(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
            status = "ET";
        }

        return status;
    }

    private articleBean articleBuilder(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        articleBean article = new articleBean();

            if (cols > 10 ) {
                article.setArtName(rs.getString("nom_article"));
                article.setArtDescrip(rs.getString("description"));
                article.getCategory().setCatName(rs.getString("libelle"));
                article.setSalePrice(rs.getInt("prix_vente"));
                article.setStartPrice(rs.getInt("prix_initial"));
                article.setStartAuc(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
                article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
                article.getPickUp().setRue(rs.getString("rue"));
                article.getPickUp().setCpo(rs.getString("code_postal"));
                article.getPickUp().setVille(rs.getString("ville"));
                article.getSeller().setPseudo(rs.getString("seller"));
                article.setSaleStatus(rs.getString("etat_vente"));
                article.setArtNb(rs.getInt("no_article"));
                article.getSeller().setTelephone(rs.getString("telephone"));
        } else {
                article.setArtName(rs.getString("nom_article"));
                article.setArtDescrip(rs.getString("description"));
                article.setStartPrice(rs.getInt("prix_initial"));
                article.setSalePrice(rs.getInt("prixdevente"));
                article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
                article.getSeller().setPseudo(rs.getString("seller"));
                article.setArtNb(rs.getInt("no_article"));
            }

        return article;
    }
}
