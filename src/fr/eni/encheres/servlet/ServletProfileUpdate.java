package fr.eni.encheres.servlet;

import fr.eni.encheres.bo.userBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profileupdate")
public class ServletProfileUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");

        // Récupère la session et son attribut "user"
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        userBean u = new userBean();

        // Assigne l'attribut de session en attribut de requête
        // Dans a JSP profil si cet attribut est le même que le pseudo de l'attribut de session
        // et que l'attribut "update" figure dans la requête
        // alors l'utilisateur peut modifier ses données de profil

        u = (userBean) session.getAttribute("user");
        request.setAttribute("pseudo", u.getPseudo());
        String update = "update";
        request.setAttribute("update", update);

        rd.forward(request, response);
    }
}
