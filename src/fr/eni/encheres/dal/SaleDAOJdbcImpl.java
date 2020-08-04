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
    private final String SELECT_ONGOING_AUCS = "select a.no_article, a.nom_article, a.description, j.pseudo as 'seller' ,e.montant_enchere, a.prix_initial, e.date_enchere, a.date_fin_encheres, a.etat_vente from ARTICLES_VENDUS a\n" +
            "inner join ENCHERES E on a.no_article = E.no_article\n" +
            "inner join UTILISATEURS U on e.no_utilisateur = U.no_utilisateur\n" +
            "inner join UTILISATEURS J on a.no_utilisateur = j.no_utilisateur\n" +
            "inner join CATEGORIES C on a.no_categorie = C.no_categorie";

    private final String SELECT_WINNING_BIDS = "select AV.nom_article, AV.description, A.max as 'prixdevente', AV.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS AV\n" +
            "inner join (SELECT max(A.montant_enchere) as 'max', A.no_article, A.no_utilisateur from ENCHERES A, ENCHERES B\n" +
            "where A.no_utilisateur <> B.no_utilisateur and A.no_article = B.no_article and A.montant_enchere > B.montant_enchere\n" +
            "group by A.no_article, A.no_utilisateur) A on A.no_article = AV.no_article\n" +
            "inner join UTILISATEURS U on U.no_utilisateur = AV.no_utilisateur\n" +
            "where A.no_utilisateur = ? and AV.etat_vente = 'ET'\n" +
            "group by A.no_article, A.no_utilisateur, AV.nom_article, AV.description, A.max, U.pseudo, AV.date_fin_encheres";

    private final String byName =  " where a.nom_article = ?";
    private final String byCat = " where a.no_categorie = ?";
    private final String byNameAndCat = " where a.nom_article = ? and a.no_categorie = ?";
    private final String EC = " and etat_vente = 'EC'";
    private final String CR = " and etat_vente = 'CR'";
    private final String ET = " and etat_vente = 'ET'";
    private final String byUserNb = " and e.no_utilisateur = ?";


    public List<articleBean> selectUserItemsForSale(Integer userNb, String name, String cat, String status) throws BusinessException {
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
        sqlstmt.append(SELECT_WINNING_BIDS);
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

            System.out.println(sqlstmt);


            PreparedStatement stmt = cnx.prepareStatement(String.valueOf(sqlstmt));

            // S'il y a un mot de recherche
            if (!name.isEmpty())
            {
                // Et s'il y a aussi une catégorie
                if (!cat.isEmpty())
                {
                    int no_cat = this.selectCatByName(cat);
                    System.out.println("Le num de la cat = " + no_cat);
                    stmt.setInt(1, userNb);
                    stmt.setString(2, name);
                    stmt.setInt(3, no_cat);

                    // s'il n'y a pas de caté et que le mot de recherche
                } else {
                    stmt.setInt(1, userNb);
                    stmt.setString(2, name);

                    System.out.println("Voici le mot clé = " + name);
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
        sqlstmt.append(SELECT_ONGOING_AUCS);
        boolean skip = false;


        // On ajoute à la requête les conditions selon les critères de recherche passés en paramètre de la requête HTTP
        // Ici le mot de recherche et la catégorie
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

        // Ici le statue de la vente
        if (!status.isEmpty() && !skip) {
            if (status.equals("EC")) {
                sqlstmt.append(EC);
            } else if (status.equals("CR")) {
                sqlstmt.append(CR);
            } else {
                sqlstmt.append(ET);
            }
        }

        // Si l'utilisateur a numéro qutre que 0, c'est qu'il possède un compte
        // Donc on ajoute aux conditions de la requête le numéro d'utilisateur
        if (userNb != 0) {
            sqlstmt.append(byUserNb);
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
                        System.out.println("Le num de la cat = " + no_cat);
                        stmt.setString(1, name);
                        stmt.setInt(2, no_cat);
                        stmt.setInt(3, userNb);
                        // s'il n'y a pas de caté et que le mot de recherche
                    } else {
                        stmt.setString(1, name);
                        stmt.setInt(2, userNb);
                        System.out.println("Voici le mot clé = " + name);
                    }
                    // s'il n'y a pas de mot de recherche
                }
                else if (!cat.isEmpty())
                {
                    int no_cat = this.selectCatByName(cat);
                    System.out.println("Le num de la cat = " + no_cat + " dans le cas où il n'y a pas de keyword");
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
        System.out.println("Le builder est mis en marche");

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
        System.out.println("Le builder est mis en marche");

        article.setArtName(rs.getString("nom_article"));
        article.setArtDescrip(rs.getString("description"));
        article.setStartPrice(rs.getInt("prixdevente"));
        article.setEndAuc(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
        article.getSeller().setPseudo(rs.getString("seller"));

        return article;
    }



}
