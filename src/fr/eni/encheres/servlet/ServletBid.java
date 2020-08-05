package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.SaleManager;
import fr.eni.encheres.bo.articleBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/enchere")
public class ServletBid extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String article = request.getParameter("article");
        SaleManager saleManager = new SaleManager();

        articleBean articledetail = new articleBean();

        request.setCharacterEncoding("UTF-8");

        try {
            articledetail = saleManager.displayAucDetail(article);
            request.setAttribute("article", articledetail);

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
