package fr.eni.encheres.dal;

import fr.eni.encheres.bo.loginBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateLogin {

    private static final String SELECT_USER_BY_ID = "select pseudo, mot_de_passe from utilisateurs where pseudo = ? and mot_de_passe = ?";

    public static boolean checkID(loginBean login) {
        boolean ok = false;
        try(Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_BY_ID);
            stmt.setString(1, login.getUsername());
            stmt.setString(2, login.getPassword());
            ResultSet rs = stmt.executeQuery();
            ok = rs.next();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ok;
    }
}
