package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Category;
import fr.eni.encheres.bo.SaleStatus;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.userBean;

import java.sql.*;

public class SaleDAOJdbcImpl {

    private final String INSERT_NEW_ARTICLE = "insert into articles_vendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie) values (?, ?, ?, ?, ?, ?, ?)";

    public articleBean inserArticle(articleBean article, userBean user, Category cat) throws BusinessException {

        // Si la méthode hérite d'un objet vide une erreur est levée
        if (article == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(INSERT_NEW_ARTICLE, PreparedStatement.RETURN_GENERATED_KEYS);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setString(1, article.getArtName());
                stmt.setString(2, article.getArtDescrip());
                stmt.setDate(3, Date.valueOf(article.getStartAuc()));
                stmt.setDate(4, Date.valueOf(article.getEndAuc()));
                stmt.setString(5, user.getTelephone());
                stmt.setInt(5, article.getStartPrice());
                stmt.setInt(6, user.getUserNb());
                stmt.setInt(7, cat.getCatNb());

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
        article.setSaleStatus(String.valueOf(SaleStatus.CREATED));
        return article;
    }
}
