package fr.eni.encheres;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        StringBuilder test = new StringBuilder("select e.montant_enchere as 'highest bid', e.no_utilisateur, u.pseudo as 'buyer', e.date_enchere from ARTICLES_VENDUS a\n" +
                "inner join ENCHERES E on a.no_article = E.no_article\n" +
                "inner join UTILISATEURS U on U.no_utilisateur = e.no_utilisateur\n" +
                "where a.nom_article = ? and a.prix_vente = e.montant_enchere");

        int index = test.indexOf("where");
        int indexend = test.indexOf("");
        test.replace(index, test.length(), "where a.prix_vente = e.montant_enchere and E.no_utilisateur = ?");
        //test.insert(index + 4, " Jolly ");

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
