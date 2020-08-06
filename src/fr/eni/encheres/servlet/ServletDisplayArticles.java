package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.SaleManager;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.biddingBean;
import fr.eni.encheres.bo.userBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class ServletDisplayArticles extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String keyword = null;
        String categorie = null;
        String searchcrit = null;
        String status = null;

        SaleManager saleManager = new SaleManager();

        try {
            saleManager.updateSaleStatus();
        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("errorList", e.getErrorList());
        }

        HttpSession session = request.getSession();

        request.setCharacterEncoding("UTF-8");

        List<articleBean> allArticles = new ArrayList<articleBean>();

        boolean search = true;
        request.setAttribute("search", true);

        // On récupère l'utilisateur s'il est connecté
        userBean user = new userBean();
        user = (userBean) session.getAttribute("user");

        // On récupère les paramètres de recherche s'il y en a
        keyword = request.getParameter("keyword");
        categorie = request.getParameter("categories");
        searchcrit = request.getParameter("searchcrit");
        status = request.getParameter("status");

        // Si le critère de recherche et toutes, la catégorie reste vide
        // Si aucun statut de vente n'est précisé, on considère que la recherche porte sur le statut EC

        if (categorie.equals("toutes")) {
            categorie = "";
        }
        if (status == null) {
            status = "EC";
        }

        // Si les recherches portent sur les achats

        if (searchcrit != null) {
            if (searchcrit.equals("achats")) {
                if (status.equals("EC")) {
                    try {
                        allArticles = saleManager.displayAllArticles(keyword, categorie, status);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        request.setAttribute("errorList", e.getErrorList());
                    }
                } else if (status.equals("MEC")) {
                    try {
                        status = "EC";
                        allArticles = saleManager.displayAuctions(user.getUserNb(), keyword, categorie, status);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        request.setAttribute("errorList", e.getErrorList());
                    }
                } else {
                    try {
                        allArticles = saleManager.displayUserWiningBids(user.getUserNb(), keyword, categorie);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        request.setAttribute("errorList", e.getErrorList());
                        request.setAttribute("win", "win");
                    }
                }
                // SI LES RECHERCHES PORTENT SUR LES VENTES
            } else if (searchcrit.equals("ventes")) {
                try {
                    allArticles = saleManager.displayUserArticlesForSale(user.getUserNb(), keyword, categorie, status);
                    request.setAttribute("allArticles", allArticles);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }
            }
            // SI LE VISITEUR N'A PAS DE COMPTE OU EST DECONNECTE
        } else {
            try {
                allArticles = saleManager.displayAllArticles(keyword, categorie, status);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            System.out.println(keyword + " " + categorie + " " + status);
        }

        if (!allArticles.isEmpty()) {
            request.setAttribute("allArticles", allArticles);
        } else {
            System.out.println("La liste d'article est vide ");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/accueil");
        rd.forward(request, response);
    }
}



