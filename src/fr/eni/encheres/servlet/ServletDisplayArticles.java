package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.BidManager;
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
        String cBoxA = null;
        String cBoxB = null;
        String cBoxC = null;


        SaleManager saleManager = new SaleManager();
        BidManager bidManager = new BidManager();

        HttpSession session = request.getSession();

        request.setCharacterEncoding("UTF-8");

        List<articleBean> allArticles = new ArrayList<articleBean>();
        List<biddingBean> bids = new ArrayList<>();
        userBean user = new userBean();

        boolean search = true;
        request.setAttribute("search", true);

        // On récupère l'utilisateur s'il est connecté

        user = (userBean) session.getAttribute("user");

        // On récupère les paramètres de recherche s'il y en a
        keyword = request.getParameter("keyword");
        categorie = request.getParameter("categories");
        searchcrit = request.getParameter("searchcrit");

        // S'il n'y a pas de mot de recherche, soit on affiche toutes les annonces, soit on affiche les résultats par caté
        if (keyword == null || keyword.trim().isEmpty()) {
            if (categorie.equals("toutes")) {
                if (searchcrit.equals("achats")) {
                    try {
                        bids = bidManager.displayOnGoingBids(user.getUserNb());
                        request.setAttribute("bids", bids);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        request.setAttribute("errorList", e.getErrorList());
                    }
                } else {
                    try {
                        allArticles = saleManager.displayAllArticles();
                        request.setAttribute("allArticles", allArticles);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        request.setAttribute("errorList", e.getErrorList());
                    }
                }
            } else {
                try {
                    allArticles = saleManager.displayArticlesSelectByCat(categorie);
                    request.setAttribute("allArticles", allArticles);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }
            }
            // S'il y a un mot de recherche, il est soit combiné avec une catégorie ou non
        } else if (categorie.equals("toutes")) {
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
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
        rd.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/accueil");
        rd.forward(request, response);
    }
}
