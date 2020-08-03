package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAOJdbcImpl implements SaleDAO {

    private final String INSERT_NEW_ARTICLE = "insert into articles_vendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie, etat_vente) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_CAT_BY_NAME = "select no_categorie from categories where libelle = ?";
    private final String SELECT_ALL_ARTICLES = "select a.nom_article, a.description, a.prix_initial, a.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur";
    private final String SELECT_ARTICLE_BY_NAME = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur, a.etat_vente from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where (a.nom_article = ? and etat_vente = 'EC')";
    private final String SELECT_ARTICLE_BY_NAME_AND_CAT = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur, a.etat_vente from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where a.nom_article = ? and no_categorie = ? and etat_vente = 'EC'";
    private final String SELECT_ARTICLE_BY_CAT = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur, a.etat_vente from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where no_categorie = ? and etat_vente = 'EC'";
    private final String SELECT_ARTICLE_BY_SELLER = "select no_article, nom_article, description, date_fin_encheres, a.no_categorie, etat_vente, c.libelle from ARTICLES_VENDUS a inner join CATEGORIES c on a.no_categorie = c.no_categorie where no_utilisateur = ?";
    private final String SELECT_BIDS_BY_BUYER = "select a.no_article, a.nom_article, a.description, c.libelle, j.pseudo as 'seller' ,e.montant_enchere, a.prix_initial, e.date_enchere, a.date_fin_encheres, a.etat_vente from ARTICLES_VENDUS a\n" +
            "inner join ENCHERES E on a.no_article = E.no_article\n" +
            "inner join UTILISATEURS U on e.no_utilisateur = U.no_utilisateur\n" +
            "inner join UTILISATEURS J on a.no_utilisateur = j.no_utilisateur\n" +
            "inner join CATEGORIES C on a.no_categorie = C.no_categorie\n" +
            "where e.no_utilisateur = ?";

    private final String byName =  " where a.nom_article = ?";
    private final String byCat = " where no_categorie = ?";
    private final String byNameAndCat = " where a.nom_article = ? and no_categorie = ?";
    private final String EC = " and etat_vente = 'EC'";
    private final String CR = " and etat_vente = 'CR'";
    private final String ET = " and etat_vente = 'ET'";


    public List<articleBean> selectAllArticles(String name, String cat, String status) throws BusinessException {
        StringBuilder sqlstmt = new StringBuilder();
        sqlstmt.append(SELECT_ALL_ARTICLES);
        boolean skip = false;

        // Vérifie les paramètres pour ajouter des conditions à la requête

        if (!name.isEmpty()) {
            if (!cat.isEmpty()) {
                sqlstmt.append(byNameAndCat);
            } else {
                sqlstmt.append(byName);
            }
        } else if (!cat.isEmpty()) {
            sqlstmt.append(byCat);
        } else {
            sqlstmt.append(" where etat_vente = ").append("'").append(status).append("'");
            skip = true;
        }

        if (!status.isEmpty() && !skip) {
            if (status.equals("EC")) {
                sqlstmt.append(EC);
            } else if (status.equals("CR")) {
                sqlstmt.append(CR);
            } else {
                sqlstmt.append(ET);
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


    public List<articleBean> selectArticlesBySeller(Integer userNb) throws BusinessException {

        if(userNb == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        List<articleBean> articles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {


            PreparedStatement stmt = cnx.prepareStatement(SELECT_ARTICLE_BY_SELLER);

            stmt.setInt(1, userNb);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                articles.add(articleBuilder(rs));
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

    public List<articleBean> selectArticlesByCat(String categorie) throws BusinessException {

        if(categorie == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        List<articleBean> articles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            // On récupère le numero de la categorie

            int no_cat = this.selectCatByName(categorie);

            PreparedStatement stmt = cnx.prepareStatement(SELECT_ARTICLE_BY_CAT);

            stmt.setInt(1, no_cat);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                articles.add(articleBuilder(rs));
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

    public List<articleBean> selectArticlesByNameAndCat(String name, String categorie) throws BusinessException {

        if(name == null || categorie == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        List<articleBean> articles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            // On récupère le numero de la categorie

            int no_cat = this.selectCatByName(categorie);

            PreparedStatement stmt = cnx.prepareStatement(SELECT_ARTICLE_BY_NAME_AND_CAT);

            stmt.setString(1, name);
            stmt.setInt(2, no_cat);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                articles.add(articleBuilder(rs));
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

    public List<articleBean> selectArticlesByName(String name) throws BusinessException {
        List<articleBean> articles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_ARTICLE_BY_NAME);

            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                articles.add(articleBuilder(rs));
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
                PreparedStatement stmt = cnx.prepareStatement(INSERT_NEW_ARTICLE, PreparedStatement.RETURN_GENERATED_KEYS);

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

            PreparedStatement stmt = cnx.prepareStatement(SELECT_CAT_BY_NAME);

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


        if (cols > 7) {
            article.getCategory().setCatName(rs.getString("libelle"));
        }

        return article;
    }

}
