package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.util.SqlStatements;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOJdbcImpl implements UserDAO {

    private static final String INSERT_NEW_USER = "insert into utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_PWD = "select mot_de_passe from utilisateurs where no_utilisateur = ?";
    private static final String SELECT_USER_BY_PSEUDO = "select pseudo from utilisateurs where pseudo = ?";
    private static final String SELECT_USER_BY_EMAIL = "select email from utilisateurs where email = ?";
    private static final String SELECT_USER_BY_ID = "select pseudo, mot_de_passe from utilisateurs where pseudo = ? and mot_de_passe = ?";
    private static final String SELECT_USER_PUBLIC_INFO = "select pseudo, nom, prenom, email, telephone, rue, code_postal, ville from utilisateurs where pseudo = ?";
    private static final String SELECT_USER_PRIVATE_INFO = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit from utilisateurs where pseudo = ?";
    private static final String UPDATE_USER_INFO = "update utilisateurs set pseudo = ?, nom = ?, prenom = ?, email = ?, telephone = ?, rue = ?, code_postal = ?, ville = ?, mot_de_passe = ? where no_utilisateur = ?";
    private static final String DELETE_USER = "delete from utilisateurs where no_utilisateur = ?";


    public void updateUserCredit(int credit, int userNb) throws BusinessException {
        try (Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(SqlStatements.UPDATE_USER_CREDIT);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, credit);
                stmt.setInt(2, userNb);

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

    public void deleteUser(int nbutilisateur) throws BusinessException {
        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(DELETE_USER);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setInt(1, nbutilisateur);

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

    public userBean updateUserInfo (userBean user) throws BusinessException {

        // Si la méthode hérite d'un objet vide une erreur est levée
        if(user == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(UPDATE_USER_INFO);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setString(1, user.getPseudo());
                stmt.setString(2, user.getNom());
                stmt.setString(3, user.getPrenom());
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getTelephone());
                stmt.setString(6, user.getRue());
                stmt.setString(7, user.getCpo());
                stmt.setString(8, user.getVille());
                stmt.setString(9, user.getMdp());
                stmt.setInt(10, user.getUserNb());

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

        return user;
    }

    public userBean selectUserPrivateInfo(String pseudo) throws BusinessException {

        userBean private_info = new userBean();

        List<String> profileInfo = new ArrayList<String>();

        // Si la méthode hérite d'un objet vide une erreur est levée

        if(pseudo == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_PRIVATE_INFO);

            // Remplit les placeholders avec le pseudo
            stmt.setString(1, pseudo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                private_info = userBuilder(rs);
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return private_info;

    }

    public userBean selectUserPublicInfo(String pseudo) throws BusinessException {

        userBean public_info = new userBean();

        List<String> profileInfo = new ArrayList<String>();

        // Si la méthode hérite d'un objet vide une erreur est levée

        if(pseudo == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        try (Connection cnx = ConnectionWizard.getConnection()) {
            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_PUBLIC_INFO);

            // Remplit les placeholders avec le pseudo
            stmt.setString(1, pseudo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                public_info = userBuilder(rs);
            }

        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        return public_info;

    }

    public userBean insertUser(userBean user) throws BusinessException {

        // Si la méthode hérite d'un objet vide une erreur est levée
        if(user == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            try {
                cnx.setAutoCommit(false);

                // assigne la requête sql au preparedstatement
                PreparedStatement stmt = cnx.prepareStatement(INSERT_NEW_USER, PreparedStatement.RETURN_GENERATED_KEYS);

                // Remplit les placeholders avec les infos passées param dans le formulaire de signup
                stmt.setString(1, user.getPseudo());
                stmt.setString(2, user.getNom());
                stmt.setString(3, user.getPrenom());
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getTelephone());
                stmt.setString(6, user.getRue());
                stmt.setString(7, user.getCpo());
                stmt.setString(8, user.getVille());
                stmt.setString(9, user.getMdp());
                stmt.setInt(10, user.getCredit());

                // Envoie la requête
                stmt.executeUpdate();

                // Déclare un variable pour stocker le no_utilisateur
                ResultSet rs = stmt.getGeneratedKeys();

                // Si une clé a été assignée par la DB, on l'enregistre dans le userBean
                if (rs.next()) {
                    user.setUserNb(rs.getInt(1));
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
        user.setConnecte(true);
        return user;
    }

    public boolean passwordIsValid(userBean login) throws BusinessException {
        boolean ok = false;
        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_BY_PWD);

            // Remplit les placeholders avec le username et password du bean passé en param
            stmt.setInt(1, login.getUserNb());
            ResultSet rs = stmt.executeQuery();

            // Assigne au booléen ok, la valeur rs.next() qui parcours le resultset

            ok = rs.next();


        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur d'authentification et l'ajoute à une liste
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_VALIDATION_PWD);
            throw bizEx;
        }
        if (!ok) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_VALIDATION_PWD);
            throw bizEx;
        }
        return ok;
    }

    public userBean checkID(userBean login) throws BusinessException {
        boolean ok = false;
        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_BY_ID);

            // Remplit les placeholders avec le username et password du bean passé en param
            stmt.setString(1, login.getPseudo());
            stmt.setString(2, login.getMdp());
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
        if (!ok) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_VALIDATION_LOGIN);
            throw bizEx;
        }
        return login;
    }

    // Méthode pour vérifier si le pseudo n'est pas déjà utilisé

    public void checkPseudo(String pseudo) throws BusinessException {
        boolean pseudoNotAvailable = false;

        // Si la méthode hérite d'un objet vide une erreur est levée
        if(pseudo == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_BY_PSEUDO);

            // Remplit les placeholders avec le username
            stmt.setString(1, pseudo);
            ResultSet rs = stmt.executeQuery();

            // Assigne au booléen ok, la valeur rs.next() qui parcours le resultset

            pseudoNotAvailable = rs.next();


        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        if (pseudoNotAvailable) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_SIGNUP_PSEUDO_INUSE);
            throw bizEx;
        }
    }

    // Méthode pour vérifier si l'email' n'est pas déjà utilisé

    public void checkEmail(String email) throws BusinessException {
        boolean emailNotAvailable = false;

        // Si la méthode hérite d'un objet vide une erreur est levée
        if(email == null) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.NULL_OBJECT_EXCEPTION);
            throw bizEx;
        }

        // tente d'ouvrir une connection à la BDD
        try(Connection cnx = ConnectionWizard.getConnection()) {

            PreparedStatement stmt = cnx.prepareStatement(SELECT_USER_BY_EMAIL);

            // Remplit les placeholders avec le username
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Assigne au booléen ok, la valeur rs.next() qui parcours le resultset

            emailNotAvailable = rs.next();


        } catch (SQLException throwables) {

            // Si erreur de lecture, on lève une erreur
            throwables.printStackTrace();
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_LECTURE_DB);
            throw bizEx;
        }

        if (emailNotAvailable) {
            BusinessException bizEx = new BusinessException();
            bizEx.addError(CodesErreurDAL.ECHEC_SIGNUP_EMAIL_INUSE);
            throw bizEx;
        }
    }

    private userBean userBuilder(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        userBean userInf = new userBean();

        if (cols > 8) {
            userInf.setUserNb(rs.getInt("no_utilisateur"));
            userInf.setPseudo(rs.getString("pseudo"));
            userInf.setNom(rs.getString("nom"));
            userInf.setPrenom(rs.getString("prenom"));
            userInf.setEmail(rs.getString("email"));
            userInf.setTelephone(rs.getString("telephone"));
            userInf.setRue(rs.getString("rue"));
            userInf.setCpo(rs.getString("code_postal"));
            userInf.setVille(rs.getString("ville"));
            userInf.setMdp(rs.getString("mot_de_passe"));
            userInf.setCredit(rs.getInt("credit"));

        } else {
            userInf.setPseudo(rs.getString("pseudo"));
            userInf.setNom(rs.getString("nom"));
            userInf.setPrenom(rs.getString("prenom"));
            userInf.setEmail(rs.getString("email"));
            userInf.setTelephone(rs.getString("telephone"));
            userInf.setRue(rs.getString("rue"));
            userInf.setCpo(rs.getString("code_postal"));
            userInf.setVille(rs.getString("ville"));
        }
        return userInf;
    }

}
