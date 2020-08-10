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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/modifconfirm")
public class ServletProfileUpdateConfirm extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userManager = new UserManager();

        // On récupère les détails de l'utilisateur courant
        HttpSession session = request.getSession();
        userBean currentUser = (userBean) session.getAttribute("user");

        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // On récupère les données du formulaire est les insère dans une hashmap
        HashMap<String, String> mdp = new HashMap<String, String>();

        // Assigne l'ensemble des paramètres à une Map
        Map parametres = request.getParameterMap();

        userBean modifs = new userBean();

        mdp.put("mdp", request.getParameter("mdp"));
        mdp.put("nouveaumdp", request.getParameter("nouveaumdp"));
        mdp.put("confirmation", request.getParameter("confirmation"));

        modifs.setPseudo(request.getParameter("pseudo"));
        modifs.setNom(request.getParameter("nom"));
        modifs.setPrenom(request.getParameter("prenom"));
        modifs.setEmail(request.getParameter("email").trim());
        modifs.setTelephone(request.getParameter("telephone"));
        modifs.setRue(request.getParameter("rue"));
        modifs.setCpo(request.getParameter("cpo"));
        modifs.setVille(request.getParameter("ville"));
        modifs.setMdp(request.getParameter("nouveaumdp"));


        try {
            // Si l'update a fonctionné, l'utilisateur est redirigé vers son profil
            userManager.updateUserProfile(parametres, currentUser, modifs, mdp, request);

            session.setAttribute("user", modifs);
            request.setAttribute("profile", modifs);

            request.setAttribute("success", "Les modifications demandées ont bien été prises en compte");

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
            rd.forward(request, response);
        } catch (BusinessException e) {
            e.printStackTrace();
            request.setAttribute("profile", currentUser);
            request.setAttribute("errorList", e.getErrorList());
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
            rd.forward(request, response);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        rd.forward(request, response);
    }
}
