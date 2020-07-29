package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.dal.UserDAOJdbcImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/signup")
public class ServletSignUp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        userBean user = new userBean();
        List<Integer> errorList = new ArrayList<>();
        UserDAOJdbcImpl daoJdbc = new UserDAOJdbcImpl();
        user.setPseudo(request.getParameter("pseudo"));
        user.setNom(request.getParameter("nom"));
        user.setPrenom(request.getParameter("prenom"));
        user.setEmail(request.getParameter("email"));
        user.setTelephone(request.getParameter("telephone"));
        user.setRue(request.getParameter("rue"));
        user.setCpo(request.getParameter("cpo"));
        user.setVille(request.getParameter("ville"));
        user.setMdp(request.getParameter("mdp"));

        try {
            daoJdbc.insertUser(user);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
            rd.forward(request, response);
        } catch (BusinessException e) {
            e.printStackTrace();
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp");
            rd.forward(request, response);

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp");
        rd.forward(request, response);
    }
}
