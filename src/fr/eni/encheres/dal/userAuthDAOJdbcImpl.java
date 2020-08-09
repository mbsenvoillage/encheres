package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userAuth;
import fr.eni.encheres.util.SqlStatements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userAuthDAOJdbcImpl implements userAuthDAO{

    public void deleteAuthToken(int id) throws BusinessException {
        try(Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.DELETE_AUTH_TOKEN_BY_ID);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, id);

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
            bizEx.addError(CodesErreurDAL.ECHEC_DELETE_OBJECT);
            throw bizEx;
        }
    }

    public void insertNewToken(userAuth token) throws BusinessException{
        // Si la méthode hérite d'un objet vide une erreur est levée
        if (token == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }


        // tente d'ouvrir une connection à la BDD
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.INSERT_NEW_AUTH_TOKEN);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setString(1, token.getSelector());
                stmt.setString(2, token.getValidator());
                stmt.setInt(3, token.getUser().getUserNb());

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

    public userAuth selectBySelector(String selector) throws BusinessException {

        userAuth auth = new userAuth();

        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SqlStatements.SELECT_VALIDATOR_BY_SELECTOR);

            // Remplit les placeholders avec le username
            stmt.setString(1, selector);
            ResultSet rs = stmt.executeQuery();

            // Assigne au booléen ok, la valeur rs.next() qui parcours le resultset

            if(rs.next()) {
                auth.setSelector(rs.getString("selector"));
                auth.setValidator(rs.getString("validator"));
                auth.getUser().setUserNb(rs.getInt("no_utilisateur"));
                auth.setId(rs.getInt("id"));
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }
        return auth;
    }
}
