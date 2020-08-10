package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.SaleManager;
import fr.eni.encheres.bo.Category;
import fr.eni.encheres.bo.PickUp;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.userBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/nouvellevente")
public class ServletUpForSale extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        SaleManager saleManager = new SaleManager();
        articleBean article = new articleBean();
        List<Integer> errorList = new ArrayList<>();

        if (request.getParameter("delete") != null && request.getParameter("delete").equals("true")) {
            try {
                saleManager.deleteArticle(Integer.parseInt(request.getParameter("artnb")));
                boolean deleted = true;
                request.setAttribute("deleted", deleted);
            } catch (BusinessException e) {
                e.printStackTrace();
                request.setAttribute("errorList", e.getErrorList());
            }
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/forsale.jsp");
            rd.forward(request, response);
            return;
        }

        request.setCharacterEncoding("UTF-8");

        // On récupère les infos de l'utilisateur courant par l'attribut de session user


        userBean user = new userBean();
        user = (userBean) session.getAttribute("user");


        // On récupère l'ensemble des paramètres

        Map parametres = request.getParameterMap();

        // On assigne les paramètres aux champs de l'instance d'article

        article.setArtName(request.getParameter("article"));
        article.setArtDescrip(request.getParameter("description"));
        article.setCategory(new Category(request.getParameter("categorie")));


        // Si aucun prix n'a été renseigné, une exception est levée

        try {
            article.setStartPrice(Integer.parseInt(request.getParameter("prix")));

        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorList.add(CodesErreurServlet.CHAMPS_VIDE_ERREUR);
        }

        // Si aucune date n'a été renseignée, une exception est levée

        try {
            article.setStartAuc(LocalDateTime.parse(request.getParameter("salestart")));
            article.setEndAuc(LocalDateTime.parse(request.getParameter("saleend")));
        } catch (Exception e) {
            e.printStackTrace();
            if (errorList.size() == 0) {
                errorList.add(CodesErreurServlet.CHAMPS_VIDE_ERREUR);
            }
        }

        if (errorList.size() > 0) {
            request.setAttribute("errorList", errorList);

        } else {
            article.setPickUp(new PickUp(request.getParameter("rue"), request.getParameter("cpo"), request.getParameter("ville")));


            if (request.getParameter("modif") != null && request.getParameter("modif").equals("true")) {

                article.getPickUp().setArtNb(Integer.parseInt(request.getParameter("artnb")));

                try {

                    if (request.getParameter("status").equals("CR")) {
                        article.setArtNb(Integer.parseInt(request.getParameter("artnb")));
                        saleManager.updateArticleForSale(parametres, article, user);
                        saleManager.updatePickUpAddress(article.getPickUp(), article.getArtNb());
                        request.setAttribute("article", article);
                        boolean modified = true;
                        request.setAttribute("modified", modified);
                    } else {
                        errorList.add(CodesErreurServlet.MODIFICATION_IMPOSSIBLE);
                    }

                } catch (BusinessException e) {
                    e.printStackTrace();
                    errorList.add(CodesErreurServlet.MODIFICATION_IMPOSSIBLE);
                    request.setAttribute("errorList", e.getErrorList());
                }

            } else {
                try {
                    saleManager.addArticleForSale(parametres, article, user);
                    saleManager.addPickUpAddress(article.getPickUp(), article.getArtNb());
                    request.setAttribute("article", article);
                    boolean added = true;
                    request.setAttribute("added", added);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());

                }
            }
        }

        if (errorList.size() > 0) {
            try {
                article = saleManager.auctionDetail(Integer.parseInt(request.getParameter("artnb")));
                request.setAttribute("article", article);
                boolean failedmodif = true;
                request.setAttribute("failedmodif", failedmodif);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/forsale.jsp");
        rd.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        userBean user = (userBean) session.getAttribute("user");


        if (user == null) {
            response.sendRedirect(request.getContextPath()+"/signup");
        } else {

            if (request.getParameter("modif") != null && request.getParameter("modif").equals("true")) {
                SaleManager saleManager = new SaleManager();

                try {
                    articleBean article = saleManager.auctionDetail(Integer.parseInt(request.getParameter("artnb")));
                    boolean added = true;
                    boolean tobemodified = true;
                    request.setAttribute("added", added);
                    request.setAttribute("tobemodified", tobemodified);
                    request.setAttribute("article", article);
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/forsale.jsp");
            rd.forward(request, response);
        }

    }
}
