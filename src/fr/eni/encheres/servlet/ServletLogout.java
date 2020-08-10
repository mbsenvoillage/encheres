package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.userAuthManager;
import fr.eni.encheres.bo.userAuth;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class ServletLogout extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        session.removeAttribute("user");
        session.removeAttribute("article");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            String selector = "";

            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("selector")) {
                    selector = cookie.getValue();
                }
            }

            if(!selector.isEmpty()) {
                userAuthManager userAuthManager = new userAuthManager();
                try {
                    userAuth token = userAuthManager.findBySelector(selector);

                    if (token != null) {
                        userAuthManager.deleteToken(token.getId());

                        Cookie cookieSelector = new Cookie("selector", "");
                        cookieSelector.setMaxAge(0);

                        Cookie cookieValidator = new Cookie("validator", "");
                        cookieValidator.setMaxAge(0);

                        response.addCookie(cookieSelector);
                        response.addCookie(cookieValidator);

                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }

        }

        response.sendRedirect(request.getContextPath() + "/");

    }
}
