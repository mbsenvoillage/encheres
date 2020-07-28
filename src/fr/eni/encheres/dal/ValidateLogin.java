package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.loginBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateLogin {

    private static final String SELECT_USER_BY_ID = "select pseudo, mot_de_passe from utilisateurs where pseudo = ? and mot_de_passe = ?";

    public static boolean checkID(loginBean login) throws BusinessException {
        boolean ok = false;
        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_BY_ID);

            // Remplit les placeholders avec le username et password du bean passé en param
            stmt.setString(1, login.getUsername());
            stmt.setString(2, login.getPassword());
            ResultSet rs = stmt.executeQuery();

            // Assigne au booléen ok, la valeur rs.next() qui parcours le resultset
            ok = rs.next();

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur d'authentification et l'ajoute à une liste
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_VALIDATION_LOGIN);
            throw bizEx;
        }
        return ok;
    }
}
