package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.BidManager;
import fr.eni.encheres.bll.SaleManager;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.biddingBean;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.util.sortResults;

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
        status = request.getParameter("status");

        if (categorie.equals("toutes")) {
            categorie = "";
        }
        if (status.isEmpty()) {
            status = "EC";
        }


        if (searchcrit != null && searchcrit.equals("achats")) {
            try {
                System.out.println("We should be here");
                allArticles = saleManager.displayAllArticles(keyword, categorie, status);
                System.out.println(keyword + " " + categorie + " " +status);

            } catch (BusinessException e) {
                e.printStackTrace();
                request.setAttribute("errorList", e.getErrorList());
            }

        } else { // ie. IF SEARCHCRIT = VENTES
            if (request.getParameter("ventenc") != null) {
                try {
                    allArticles = saleManager.displayUserArticlesForSale(user.getUserNb());
                    request.setAttribute("allArticles", allArticles);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }
            } else if (request.getParameter("ventnondeb") != null) {
                try {
                    allArticles = sortResults.getUserNonStartedSales(saleManager.displayUserArticlesForSale(user.getUserNb()));
                    request.setAttribute("allArticles", allArticles);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }
            } else {
                try {
                    allArticles = sortResults.getUserFinishedSales(saleManager.displayUserArticlesForSale(user.getUserNb()));
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }
            }
        }

        /*if (categorie != null) {
            if (!allArticles.isEmpty()) {
                allArticles = sortResults.filterArticlesByCat(allArticles, categorie);
            }
            if (!bids.isEmpty()) {
                bids = sortResults.filterBidsByCat(bids, categorie);
            }
        }

        if (keyword != null) {
            if (!allArticles.isEmpty()) {
                allArticles = sortResults.filterArticlesByKeyword(allArticles, keyword);
            }
            if (!bids.isEmpty()) {
                bids = sortResults.filterBidsByKeyword(bids, keyword);
            }
        }

         */

        if (!allArticles.isEmpty()) {
            request.setAttribute("allArticles", allArticles);
        }
        if (!bids.isEmpty()) {
            request.setAttribute("bids", bids);
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
        rd.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/accueil");
        rd.forward(request, response);
    }
}


/*
 if (request.getParameter("enchouv") != null) {

            } else if (request.getParameter("enchenc") != null) {
                try {
                    bids = sortResults.getBidsForOngoingAuc(bidManager.displayOnGoingBids(user.getUserNb()));

                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }

            } else  { // ie. l'utilisateur veut connaître les enchères qu'il a remportées
                try {
                    bids = sortResults.getAuctionWins(bidManager.displayOnGoingBids(user.getUserNb()));
                    request.setAttribute("bids", bids);
                } catch (BusinessException e) {
                    e.printStackTrace();
                    request.setAttribute("errorList", e.getErrorList());
                }
            }
 */
