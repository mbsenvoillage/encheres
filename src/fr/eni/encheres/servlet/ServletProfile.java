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

        // L'utilisateur courant est stocké dans une variable
        currentuser = (userBean) session.getAttribute("user");


        // On récupère le paramètre pseudo qui doit permettre d'accéder au profil voulu
        // pour les non propriétaires du compte

        String pseudo = request.getParameter("pseudo");

        // S'il n'y a pas d'utilisateur courant ou s'il y en a un mais que son pseudo
        // est différent du profil demandé alors on ne montrera que des infos et fonctionnalités publiques

        if (currentuser == null || (pseudo != null && !pseudo.equals(currentuser.getPseudo()))) {
            try {
                UserManager userManager = new UserManager();
                userBean queriedProfile = new userBean();
                queriedProfile = userManager.displayUserPublicInfo(pseudo);
                request.setAttribute("profilepublicinfo", queriedProfile);
            } catch (BusinessException e) {
                e.printStackTrace();
                System.out.println("Something went wrong");
            }
        } else {

            // Autrement, on montre les infos privées
            boolean display = true;
            request.setAttribute("display", display);
        }


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        rd.forward(request, response);
    }
}
