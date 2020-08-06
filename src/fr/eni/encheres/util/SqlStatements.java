package fr.eni.encheres.util;

public abstract class SqlStatements {

    public static String INSERT_NEW_ARTICLE = "insert into articles_vendus (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie, etat_vente) values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static String SELECT_CAT_BY_NAME = "select no_categorie from categories where libelle = ?";

    public static String SELECT_ONGOING_AUCS = "select AV.nom_article, AV.description, AV.prix_initial, A.max as 'prixdevente', AV.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS AV\n" +
    "inner join (SELECT max(A.montant_enchere) as 'max', A.no_article, A.no_utilisateur from ENCHERES A, ENCHERES B\n" +
    "where A.no_utilisateur = B.no_utilisateur and A.no_article = B.no_article\n" +
    "group by A.no_article, A.no_utilisateur) A on A.no_article = AV.no_article\n" +
    "inner join UTILISATEURS U on U.no_utilisateur = AV.no_utilisateur\n";

    public static String SELECT_WINNING_BIDS = "select AV.nom_article, AV.description, A.max as 'prixdevente', AV.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS AV\n" +
    "inner join (SELECT max(A.montant_enchere) as 'max', A.no_article, A.no_utilisateur from ENCHERES A, ENCHERES B\n" +
    "where A.no_utilisateur <> B.no_utilisateur and A.no_article = B.no_article and A.montant_enchere > B.montant_enchere\n" +
    "group by A.no_article, A.no_utilisateur) A on A.no_article = AV.no_article\n" +
    "inner join UTILISATEURS U on U.no_utilisateur = AV.no_utilisateur\n" +
    "where A.no_utilisateur = ? and AV.etat_vente = 'ET'\n" +
    "group by A.no_article, A.no_utilisateur, AV.nom_article, AV.description, A.max, U.pseudo, AV.date_fin_encheres";

    public static String INSERT_NEW_BID = "insert into ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) values (?, ?, ?, ?);";

    public static String UPDATE_USER_CREDIT = "update UTILISATEURS set credit = credit + ? where no_utilisateur = ?";

    public static String UPDATE_ARTICLE_SALE_PRICE = "update ARTICLES_VENDUS set prix_vente = ? where no_article = ?";

    public static String CHECK_USER_CREDIT = "select credit from UTILISATEURS where no_utilisateur = ?";

    public static String SELECT_AUCTION_DETAIL = "select a.nom_article, a.description, c.libelle, a.prix_vente, a.prix_initial, a.date_fin_encheres, r.rue, r.code_postal, r.ville, u.pseudo as 'seller', a.etat_vente, a.no_article from ARTICLES_VENDUS a\n" +
            "left join utilisateurs u on a.no_utilisateur = u.no_utilisateur\n" +
            "left join CATEGORIES C on C.no_categorie = a.no_categorie\n" +
            "left join RETRAITS R on a.no_article = R.no_article\n" +
            "where nom_article = ?\n" +
            "group by a.nom_article, a.description, c.libelle, a.prix_vente, a.prix_initial, a.date_fin_encheres, r.rue, r.code_postal, r.ville, u.pseudo, a.etat_vente, a.no_article";

    public static String SELECT_HIGHEST_BIDDER = "select e.montant_enchere as 'highest bid', e.no_utilisateur, u.pseudo as 'buyer', e.date_enchere from ARTICLES_VENDUS a\n" +
            "inner join ENCHERES E on a.no_article = E.no_article\n" +
            "inner join UTILISATEURS U on U.no_utilisateur = e.no_utilisateur\n" +
            "where a.nom_article = ? and a.prix_vente = e.montant_enchere";

    public static String INSERT_PICKUP_DETAIL = "insert into RETRAITS (no_article, rue, code_postal, ville) values (?, ?, ?, ?)";

    public static String SET_STATUS_TO_EC = "update ARTICLES_VENDUS set etat_vente = 'EC' where date_debut_encheres  < CURRENT_TIMESTAMP and date_fin_encheres > CURRENT_TIMESTAMP";
    public static String SET_STATUS_TO_ET = "update ARTICLES_VENDUS set etat_vente = 'ET' where date_debut_encheres  < CURRENT_TIMESTAMP and date_fin_encheres < CURRENT_TIMESTAMP";

    public static String byName =  " where a.nom_article = ?";
    public static String byCat = " where a.no_categorie = ?";
    public static String byNameAndCat = " where a.nom_article = ? and a.no_categorie = ?";
    public static String EC = " and etat_vente = 'EC'";
    public static String CR = " and etat_vente = 'CR'";
    public static String ET = " and etat_vente = 'ET'";
    public static String byUserNb = " and e.no_utilisateur = ?";
}
