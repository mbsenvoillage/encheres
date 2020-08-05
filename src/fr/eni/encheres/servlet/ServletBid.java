package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.CodesErreurBLL;
import fr.eni.encheres.bll.SaleManager;
import fr.eni.encheres.bll.UserManager;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.biddingBean;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.dal.CodesErreurDAL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/enchere")
public class ServletBid extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        userBean user = new userBean();
        user = (userBean) session.getAttribute("user");
        articleBean article = (articleBean) session.getAttribute("article");
        int bidAmount = 0;
        SaleManager saleManager = new SaleManager();

        List<Integer> errorList = new ArrayList<>();

        // ON RECUPERE LE MONTANT DE LA PROPOSITION
        try {
            bidAmount = Integer.parseInt(request.getParameter("bidAmount"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorList.add(CodesErreurServlet.ERREUR_FORMAT_PRIX);
        }

        // SI ci montant est inférieur au prix de vente, on ajoute une erreur à la liste
        if(bidAmount <= article.getSalePrice()) {
            errorList.add(CodesErreurServlet.MONTANT_INSUFFISANT);
        }

        // Si l'utilisateur n'as pas suffisamment de fonds pour enchérir, on ajoute une erreur
        try {
            if (saleManager.getAccountBalance(user.getUserNb()) - bidAmount < 0) {
                errorList.add(CodesErreurServlet.FONDS_INSUFFISANTS);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            errorList.add(CodesErreurDAL.ECHEC_LECTURE_DB);
        }

        // S'il y a des erreurs on passe la liste en param de requête
        if (errorList.size() > 0) {
            request.setAttribute("errorList", errorList);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");
            rd.forward(request, response);
        } else {

            biddingBean bid = new biddingBean(bidAmount, LocalDateTime.now(), article.getArtNb(), user.getUserNb());
            UserManager userManager = new UserManager();

            try {
                saleManager.placeBid(bid);
                saleManager.updateSalePrice(bidAmount, article.getArtNb());
                userManager.updateUserCredit((-bidAmount), user.getUserNb());

                System.out.println(article.getSalePrice() + " " + " " +  article.getBid().getBuyerId());

                userManager.updateUserCredit(article.getSalePrice(), article.getBid().getBuyerId());

                article.setSalePrice(bidAmount);

                article = saleManager.auctionDetail(article.getArtName());
                request.setAttribute("article", article);
                request.setAttribute("success", "Votre enchère a bien été enregistrée.");
            } catch (BusinessException e) {
                e.printStackTrace();
                request.setAttribute("errorList", e.getErrorList());
            }
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");
            rd.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String article = request.getParameter("article");
        SaleManager saleManager = new SaleManager();
        HttpSession session = request.getSession();

        articleBean articledetail = new articleBean();

        request.setCharacterEncoding("UTF-8");

        try {
            //articledetail = saleManager.displayAucDetail(article);
            articledetail = saleManager.auctionDetail(article);
            session.setAttribute("article", articledetail);
            //System.out.println(articledetail.endAucToLocalDate());

        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("errorList", e.getErrorList());
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");
            rd.forward(request, response);

        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");
        rd.forward(request, response);
    }
}
