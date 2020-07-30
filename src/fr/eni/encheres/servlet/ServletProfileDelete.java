package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.UserManager;
import fr.eni.encheres.bo.userBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/delete")
public class ServletProfileDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager usmng = new UserManager();
        HttpSession session = request.getSession();
        userBean user = new userBean();
        user = (userBean) session.getAttribute("user");
        try {
            usmng.supprimerUtilisateur(user);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        session.removeAttribute("user");

        response.sendRedirect(request.getContextPath() + "/");
    }
}
