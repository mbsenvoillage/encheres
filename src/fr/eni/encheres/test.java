package fr.eni.encheres;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        List<Integer> A = new ArrayList<>();
        List<Integer> B = new ArrayList<>();

        A.add(1);
        A.add(2);
        B.add(3);
        B.add(4);
        List<Integer> C = new ArrayList<Integer>(A);
        C.addAll(B);
        C.forEach((element) -> {
            System.out.println(element);
        });
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
