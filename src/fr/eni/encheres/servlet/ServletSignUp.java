package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.UserManager;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.dal.UserDAO;
import fr.eni.encheres.dal.UserDAOJdbcImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/signup")
public class ServletSignUp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserManager userManager = new UserManager();

        String pseudo = request.getParameter("pseudo");
        String email = request.getParameter("email");

        // Assigne l'ensemble des paramètres à une Map
        Map parametres = request.getParameterMap();

        userBean user = new userBean();

        user.setPseudo(request.getParameter("pseudo"));
        user.setNom(request.getParameter("nom"));
        user.setPrenom(request.getParameter("prenom"));
        user.setEmail(request.getParameter("email").trim());
        user.setTelephone(request.getParameter("telephone"));
        user.setRue(request.getParameter("rue"));
        user.setCpo(request.getParameter("cpo"));
        user.setVille(request.getParameter("ville"));
        user.setMdp(request.getParameter("mdp"));

        try {
            // Si l'insertion fonctionne, l'utilisateur est redirigé vers son profil
            userManager.addUser(parametres, pseudo, email, user, request);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
            rd.forward(request, response);
        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("errorList", e.getErrorList());
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp");
            rd.forward(request, response);

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp");
        rd.forward(request, response);
    }
}
// TODO handle the case where no phone number is provided