package fr.eni.encheres.util;

public abstract class SqlStatements {

    public static String INSERT_NEW_ARTICLE = "insert into articles_vendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie, etat_vente) values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static String SELECT_CAT_BY_NAME = "select no_categorie from categories where libelle = ?";
    public static String SELECT_ALL_ARTICLES = "select a.nom_article, a.description, a.prix_initial, a.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS a inner join UTILISATEURS U on a.no_utilisateur = U.no_utilisateur";
    public static String SELECT_ONGOING_AUCS = "select a.no_article, a.nom_article, a.description, j.pseudo as 'seller' ,e.montant_enchere, a.prix_initial, e.date_enchere, a.date_fin_encheres, a.etat_vente from ARTICLES_VENDUS a\n" +
            "inner join ENCHERES E on a.no_article = E.no_article\n" +
            "inner join UTILISATEURS U on e.no_utilisateur = U.no_utilisateur\n" +
            "inner join UTILISATEURS J on a.no_utilisateur = j.no_utilisateur\n" +
            "inner join CATEGORIES C on a.no_categorie = C.no_categorie";

    public static String SELECT_WINNING_BIDS = "select AV.nom_article, AV.description, A.max as 'prixdevente', AV.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS AV\n" +
            "inner join (SELECT max(A.montant_enchere) as 'max', A.no_article, A.no_utilisateur from ENCHERES A, ENCHERES B\n" +
            "where A.no_utilisateur <> B.no_utilisateur and A.no_article = B.no_article and A.montant_enchere > B.montant_enchere\n" +
            "group by A.no_article, A.no_utilisateur) A on A.no_article = AV.no_article\n" +
            "inner join UTILISATEURS U on U.no_utilisateur = AV.no_utilisateur\n" +
            "where A.no_utilisateur = ? and AV.etat_vente = 'ET'\n" +
            "group by A.no_article, A.no_utilisateur, AV.nom_article, AV.description, A.max, U.pseudo, AV.date_fin_encheres";

    /*public static String SELECT_AUCTION_ARTICLE = "select a.nom_article, a.description, c.libelle, a.prix_vente, a.prix_initial, a.date_fin_encheres, a.etat_vente, r.rue, r.code_postal, r.ville, u.pseudo as 'seller' from ARTICLES_VENDUS a\n" +
            "inner join utilisateurs u on a.no_utilisateur = u.no_utilisateur\n" +
            "inner join CATEGORIES C on C.no_categorie = a.no_categorie\n" +
            "inner join RETRAITS R on a.no_article = R.no_article\n" +
            "where nom_article = ?";*/

    public static String SELECT_AUCTION_ARTICLE = "select a.nom_article, a.description, c.libelle, a.prix_vente, a.prix_initial, a.date_fin_encheres, r.rue, r.code_postal, r.ville, u.pseudo as 'seller', b.pseudo as 'buyer', a.etat_vente from ARTICLES_VENDUS a\n" +
            "inner join utilisateurs u on a.no_utilisateur = u.no_utilisateur\n" +
            "inner join CATEGORIES C on C.no_categorie = a.no_categorie\n" +
            "inner join RETRAITS R on a.no_article = R.no_article\n" +
            "inner join ENCHERES e on a.no_article = E.no_article\n" +
            "inner join UTILISATEURS b on e.no_utilisateur = b.no_utilisateur\n" +
            "where nom_article = ? and e.montant_enchere = a.prix_vente\n" +
            "group by a.nom_article, a.description, c.libelle, a.prix_vente, a.prix_initial, a.date_fin_encheres, r.rue, r.code_postal, r.ville, u.pseudo, b.pseudo, a.etat_vente";

    public static String byName =  " where a.nom_article = ?";
    public static String byCat = " where a.no_categorie = ?";
    public static String byNameAndCat = " where a.nom_article = ? and a.no_categorie = ?";
    public static String EC = " and etat_vente = 'EC'";
    public static String CR = " and etat_vente = 'CR'";
    public static String ET = " and etat_vente = 'ET'";
    public static String byUserNb = " and e.no_utilisateur = ?";
}
