package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.UserManager;
import fr.eni.encheres.bo.userBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profil")
public class ServletProfile extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // On récupère l'utilisateur courant, par la session
        HttpSession session = request.getSession();
        userBean currentuser = new userBean();
        String pseudo = request.getParameter("pseudo");
        currentuser = (userBean) session.getAttribute("user");

        if (session.getAttribute("user") == null && pseudo == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // S'il n'y a pas d'utilisateur courant ou s'il y en a un mais que son pseudo
        // est différent du profil demandé alors on ne montrera que des infos et fonctionnalités publiques

        UserManager userManager = new UserManager();
        userBean profile = new userBean();

        if (pseudo != null) {
            if (session.getAttribute("user") == null || !pseudo.equals(currentuser.getPseudo())) {
                request.setAttribute("visitor", "visitor");
                try {
                    profile = userManager.getUserPrivateInfo(pseudo);
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            } else {
                profile = currentuser;
            }
        } else {
            profile = currentuser;
        }

        request.setAttribute("profile", profile);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        rd.forward(request, response);
    }
}
