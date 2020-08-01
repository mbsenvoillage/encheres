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
import java.time.LocalDate;
import java.util.Map;

@WebServlet("/nouvellevente")
public class ServletUpForSale extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SaleManager saleManager = new SaleManager();
        articleBean article = new articleBean();

        // On récupère les infos de l'utilisateur courant par l'attribut de session user

        HttpSession session = request.getSession();
        userBean user = new userBean();
        user = (userBean) session.getAttribute("user");

        // On récupère l'ensemble des paramètres

        Map parametres = request.getParameterMap();

        // On assigne les paramètres aux champs de l'instance d'article

        article.setArtName(request.getParameter("article"));
        article.setArtDescrip(request.getParameter("description"));
        article.setCategory(new Category(request.getParameter("categorie")));
        System.out.println("This is the cat name : " + request.getParameter("categorie"));
        article.setStartPrice(Integer.parseInt(request.getParameter("prix")));
        article.setStartAuc(LocalDate.parse(request.getParameter("salestart")));
        article.setEndAuc(LocalDate.parse(request.getParameter("saleend")));
        article.setPickUp(new PickUp(request.getParameter("rue"), request.getParameter("cpo"), request.getParameter("ville")));

        try {
            saleManager.addArticleForSale(parametres, article, user);
            request.setAttribute("article", article);
            boolean added = true;
            request.setAttribute("added", added);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/forsale.jsp");
            rd.forward(request, response);
        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("errorList", e.getErrorList());
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/forsale.jsp");
            rd.forward(request, response);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/forsale.jsp");
        rd.forward(request, response);
    }
}
