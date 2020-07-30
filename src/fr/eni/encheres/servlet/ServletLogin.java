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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ServletLogin")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        userBean user = new userBean();
        List<Integer> errorList = new ArrayList<>();
        UserManager userManager = new UserManager();

        // Assigne aux variables de la classe loginBean la valeur des params userID et password
        user.setPseudo(request.getParameter("userID"));
        user.setMdp(request.getParameter("password"));

        // Si ces champs sont vides, une erreur correspondante est ajoutée à la liste
        if (user.getPseudo().trim().isEmpty() || user.getMdp().trim().isEmpty()) {
            errorList.add(CodesErreurServlet.CHAMPS_VIDE_ERREUR);
        }

        // Si la liste d'erreur n'est pas vide, on renvoie directement à la page login avec la liste d'erreur en attribut
        if (errorList.size() > 0) {
            request.setAttribute("errorList", errorList);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            rd.forward(request, response);
        } else {
            try {
                // Vérifie si l'authentification est réussie
                userManager.checkCredentials(user);

                // ValidateLogin.checkID(user); old code from before loginDAO and loginManager

                user = userManager.getUserPrivateInfo(request.getParameter("userID"));

                // Si oui, l'utilisateur est connecte
                user.setConnecte(true);

                // On recupere la session
                HttpSession session = request.getSession();

                // On lui assigne l'utilisateur en attribut
                session.setAttribute("user", user);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/accueil.jsp");
                rd.forward(request, response);

            } catch (BusinessException e) {
                e.printStackTrace();
                request.setAttribute("errorList", e.getErrorList());
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
                rd.forward(request, response);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        rd.forward(request, response);
    }
}
