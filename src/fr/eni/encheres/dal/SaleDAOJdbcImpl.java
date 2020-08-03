package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Category;
import fr.eni.encheres.bo.SaleStatus;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.userBean;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDAOJdbcImpl implements SaleDAO {

    private final String INSERT_NEW_ARTICLE = "insert into articles_vendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie, etat_vente) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_CAT_BY_NAME = "select no_categorie from categories where libelle = ?";
    private final String SELECT_ALL_ARTICLES = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where etat_vente = 'EC'";
    private final String SELECT_ARTICLE_BY_NAME = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where (a.nom_article = ? and etat_vente = 'EC')";
    private final String SELECT_ARTICLE_BY_NAME_AND_CAT = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where a.nom_article = ? and no_categorie = ? and etat_vente = 'EC'";
    private final String SELECT_ARTICLE_BY_CAT = "select a.nom_article, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, U.pseudo, U.no_utilisateur from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur where no_categorie = ? and etat_vente = 'EC'";
    private final String SELECT_ARTICLE_BY_SELLER = "select no_article, nom_article, description, date_fin_encheres, no_categorie, etat_vente from ARTICLES_VENDUS where no_utilisateur = ?";


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


    public List<articleBean> selectAllArticles() throws BusinessException {

        List<articleBean> allArticles = new ArrayList<articleBean>();


        try (Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_ALL_ARTICLES);

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
        articleBean article = new articleBean();

        article.setArtName(rs.getString("nom_article"));
        article.setStartPrice(rs.getInt("prix_initial"));
        article.setStartAuc(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
        article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
        article.getSeller().setPseudo(rs.getString("pseudo"));
        article.getSeller().setUserNb(rs.getInt("no_utilisateur"));

        return article;
    }
}
