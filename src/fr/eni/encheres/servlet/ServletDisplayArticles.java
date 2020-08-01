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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class ServletDisplayArticles extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SaleManager saleManager = new SaleManager();

        List<articleBean> allArticles = new ArrayList<articleBean>();

        try {
            allArticles = saleManager.displayAllArticles();
            request.setAttribute("allArticles", allArticles);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
            rd.forward(request, response);
        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("errorList", e.getErrorList());
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
            rd.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/accueil");
        rd.forward(request, response);
    }
}
