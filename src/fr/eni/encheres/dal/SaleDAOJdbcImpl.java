package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.*;
import fr.eni.encheres.util.SqlStatements;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAOJdbcImpl implements SaleDAO {

    public biddingBean selectHighestBidder(String artName) throws BusinessException {
        biddingBean bid = new biddingBean();

        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SELECT_HIGHEST_BIDDER);
            System.out.println(SqlStatements.SELECT_HIGHEST_BIDDER);
            stmt.setString(1, artName);
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


    public articleBean detailAuction(String artName) throws BusinessException {
        articleBean article = new articleBean();
        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SELECT_AUCTION_DETAIL);
            System.out.println(SqlStatements.SELECT_AUCTION_DETAIL);
            stmt.setString(1, artName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                article = articleBuilderTwo(rs);
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


    // THIS ONE WORKS
    public List<articleBean> selectUserWiningBids(Integer userNb, String name, String cat) throws BusinessException {


        StringBuilder sqlstmt = new StringBuilder();
        sqlstmt.append(SqlStatements.SELECT_WINNING_BIDS);
        int index = sqlstmt.indexOf("'ET'");


        // On ajoute à la requête les conditions selon les critères de recherche passés en paramètre de la requête HTTP
        // Ici le mot de recherche et la catégorie
        if (!name.isEmpty()) {
            if (!cat.isEmpty()) {
                sqlstmt.insert(index + 4, " and AV.nom_article = ? and no_categorie = ?");
            } else {
                sqlstmt.insert(index + 4, " and AV.nom_article = ?");
            }
        } else if (!cat.isEmpty()) {
            sqlstmt.insert(index + 4, " and no_categorie = ?");
        } else {
            boolean donothing;
        }

        List<articleBean> articles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            //System.out.println(sqlstmt);


            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));

            // S'il y a un mot de recherche
            if (!name.isEmpty())
            {
                // Et s'il y a aussi une catégorie
                if (!cat.isEmpty())
                {
                    int no_cat = this.selectCatByName(cat);
                    stmt.setInt(1, userNb);
                    stmt.setString(2, name);
                    stmt.setInt(3, no_cat);

                    // s'il n'y a pas de caté et que le mot de recherche
                } else {
                    stmt.setInt(1, userNb);
                    stmt.setString(2, name);
                }
                // s'il n'y a pas de mot de recherche
            }
            else if (!cat.isEmpty())
            {
                int no_cat = this.selectCatByName(cat);
                System.out.println("Le num de la cat = " + no_cat + " dans le cas où il n'y a pas de keyword");
                stmt.setInt(1, userNb);
                stmt.setInt(2, no_cat);

            }
            else
            {
                stmt.setInt(1, userNb);
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                articles.add(articleBuilderTwo(rs));
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return articles;
    }


    // THIS ONE WORKS
    // Effectue les requêtes sql pour le mode connecté et la catégorie achats

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
            sqlstmt.append(" where etat_vente = ").append("'").append(status).append("'");
            skip = true;
        }

        // Ici le statue de la vente
        if (!status.isEmpty() && !skip) {
            if (status.equals("EC")) {
                sqlstmt.append(SqlStatements.EC);
            } else if (status.equals("CR")) {
                sqlstmt.append(SqlStatements.CR);
            } else {
                sqlstmt.append(SqlStatements.ET);
            }
        }

        // Si l'utilisateur a numéro qutre que 0, c'est qu'il possède un compte
        // Donc on ajoute aux conditions de la requête le numéro d'utilisateur
        if (userNb != 0) {
            sqlstmt.append(SqlStatements.byUserNb);
            System.out.println("Le no utilisateur = " + userNb);
        }

        List<articleBean> allArticles = new ArrayList<articleBean>();
        System.out.println(sqlstmt);


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));

            // Execution de la requête pour les utilisateurs ayant un compte
            if (userNb != 0) {

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
        // Exécution de la requête pour les utilisateurs qui n'ont pas de compte
            } else {
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


    //THIS ONE WORKS TOO

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

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return allArticles;
    }

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
                stmt.setInt(6, user.getUserNb());
                stmt.setInt(7, article.getCategory().getCatNb());
                stmt.setString(8, status);

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

        article.setArtName(rs.getString("nom_article"));
        article.setArtDescrip(rs.getString("description"));
        article.setStartPrice(rs.getInt("prix_initial"));
        article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
        article.getSeller().setPseudo(rs.getString("seller"));

        return article;
    }


    private articleBean articleBuilderTwo(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        articleBean article = new articleBean();



        if (cols > 12) {
            article.setArtName(rs.getString("nom_article"));
            article.setArtDescrip(rs.getString("description"));
            article.getCategory().setCatName(rs.getString("libelle"));
            article.setSalePrice(rs.getInt("prix_vente"));
            article.setStartPrice(rs.getInt("prix_initial"));
            article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            System.out.println(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            article.getPickUp().setRue(rs.getString("rue"));
            article.getPickUp().setCpo(rs.getString("code_postal"));
            article.getPickUp().setVille(rs.getString("ville"));
            article.getSeller().setPseudo(rs.getString("seller"));
            article.getBid().setBuyerName(rs.getString("buyer"));
            article.setSaleStatus(rs.getString("etat_vente"));
            article.setArtNb(rs.getInt("no_article"));
            article.getBid().setBuyerId(rs.getInt("no_utilisateur"));
        } else if (cols > 10 ) {
            article.setArtName(rs.getString("nom_article"));
            article.setArtDescrip(rs.getString("description"));
            article.getCategory().setCatName(rs.getString("libelle"));
            article.setSalePrice(rs.getInt("prix_vente"));
            article.setStartPrice(rs.getInt("prix_initial"));
            article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            System.out.println(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            article.getPickUp().setRue(rs.getString("rue"));
            article.getPickUp().setCpo(rs.getString("code_postal"));
            article.getPickUp().setVille(rs.getString("ville"));
            article.getSeller().setPseudo(rs.getString("seller"));
            article.setSaleStatus(rs.getString("etat_vente"));
            article.setArtNb(rs.getInt("no_article"));
        } else {
            article.setArtName(rs.getString("nom_article"));
            article.setArtDescrip(rs.getString("description"));
            article.setStartPrice(rs.getInt("prixdevente"));
            article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            article.getSeller().setPseudo(rs.getString("seller"));
        }







        return article;
    }



}
