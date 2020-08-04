package fr.eni.encheres;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        StringBuilder test = new StringBuilder("\"select AV.nom_article, AV.description, A.max as 'prixdevente', AV.date_fin_encheres, U.pseudo as 'seller' from ARTICLES_VENDUS AV\\n\" +\n" +
                "            \"inner join (SELECT max(A.montant_enchere) as 'max', A.no_article, A.no_utilisateur from ENCHERES A, ENCHERES B\\n\" +\n" +
                "            \"where A.no_utilisateur <> B.no_utilisateur and A.no_article = B.no_article and A.montant_enchere > B.montant_enchere\\n\" +\n" +
                "            \"group by A.no_article, A.no_utilisateur) A on A.no_article = AV.no_article\\n\" +\n" +
                "            \"inner join UTILISATEURS U on U.no_utilisateur = AV.no_utilisateur\\n\" +\n" +
                "            \"where A.no_utilisateur = ? and AV.etat_vente = 'ET'\\n\" +\n" +
                "            \"group by A.no_article, A.no_utilisateur, AV.nom_article, AV.description, A.max, U.pseudo, AV.date_fin_encheres\"");
        int index = test.indexOf("'ET'");
        test.insert(index + 4, " Jolly ");

        System.out.println(test);

    }

        /*
        try {
            allArticles = saleManager.displayArticlesSelectByCat(categorie);
            request.setAttribute("allArticles", allArticles);
        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("errorList", e.getErrorList());
        }
    }
    // S'il y a un mot de recherche, il est soit combiné avec une catégorie ou non
    else if (categorie.equals("toutes")) {
        try {
        allArticles = saleManager.displayArticlesSelectByName(keyword);
        request.setAttribute("allArticles", allArticles);
        } catch (BusinessException e) {
        e.printStackTrace();
        request.setAttribute("errorList", e.getErrorList());
        }
        } else {
        try {
        allArticles = saleManager.displayArticlesSelectByNameAndCat(keyword, categorie);
        request.setAttribute("allArticles", allArticles);
        } catch (BusinessException e) {
        e.printStackTrace();
        request.setAttribute("errorList", e.getErrorList());
        }

     */

}
