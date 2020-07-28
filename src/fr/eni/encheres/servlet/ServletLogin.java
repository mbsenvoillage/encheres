package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.loginBean;
import fr.eni.encheres.dal.ValidateLogin;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ServletLogin")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loginBean login = new loginBean();
        List<Integer> errorList = new ArrayList<>();

        // Assigne aux variables de la classe loginBean la valeur des params userID et password
        login.setUsername(request.getParameter("userID"));
        login.setPassword(request.getParameter("password"));

        // Si ces champs sont vide, une erreur correspondante est ajoutée à la liste
        if (login.getUsername().trim().isEmpty() || login.getPassword().trim().isEmpty()) {
            errorList.add(CodesErreurServlet.CHAMPS_VIDE_ERREUR);
        }

        // Si la liste d'erreur n'est pas vide, on renvoie directement à la page login avec la liste d'erreur en attribut
        if (errorList.size() > 0) {
            request.setAttribute("errorList", errorList);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            rd.forward(request, response);
        } else {
            try {
                ValidateLogin.checkID(login);
                request.setAttribute("user", login);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/loginsuccess.jsp");
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
