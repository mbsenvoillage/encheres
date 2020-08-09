package fr.eni.encheres.servlet;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.UserManager;
import fr.eni.encheres.bll.userAuthManager;
import fr.eni.encheres.bo.userAuth;
import fr.eni.encheres.bo.userBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet("/login")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        userBean user = new userBean();
        List<Integer> errorList = new ArrayList<>();
        UserManager userManager = new UserManager();

        // On recupere la session
        HttpSession session = request.getSession();


        // Assigne aux variables de la classe loginBean la valeur des params userID et password
        user.setPseudo(request.getParameter("userID"));
        user.setMdp(request.getParameter("password"));

        // Récupère la valeur de l'attribut se souvenir de moi

        boolean rememberme = "true".equals(request.getParameter("rememberme"));

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

                if (rememberme) {
                    userAuth newToken = new userAuth();
                    String selector = RandomStringUtils.randomAlphanumeric(12);
                    String rawValidator = RandomStringUtils.randomAlphanumeric(64);
                    String hashedValidator = DigestUtils.sha256Hex(rawValidator);

                    newToken.setSelector(selector);
                    newToken.setValidator(hashedValidator);

                    newToken.setUser(user);

                    userAuthManager authManager = new userAuthManager();
                    authManager.createNewAuthToken(newToken);

                    Cookie cookieSelector = new Cookie("selector", selector);
                    cookieSelector.setMaxAge(86400*15);

                    Cookie cookieValidator = new Cookie("validator", rawValidator);
                    cookieValidator.setMaxAge(86400*15);

                    response.addCookie(cookieSelector);
                    response.addCookie(cookieValidator);
                }


                // On lui assigne l'utilisateur en attribut
                session.setAttribute("user", user);

                // On limite la durée de vie maximale de la session à 5 min

                session.setMaxInactiveInterval(5*60);

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


        // On recupere la session
        HttpSession session = request.getSession();

        // On vérifie si l'utilisateur est connecté
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        // On récupère les cookies
        Cookie[] cookies = request.getCookies();

        // Si l'utilisateur n'est pas encore connecté et que son browser a renvoyé des cookies, alors on peut procéder à la vérification du rememberme
        if(!loggedIn && cookies != null) {
            String selector = "";
            String rawvalidator = "";

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("selector")) {
                    selector = cookie.getValue();
                } else if (cookie.getName().equals("validator")) {
                    rawvalidator = cookie.getValue();
                }
            }

            if (!"".equals(selector) && !"".equals(rawvalidator)) {
                userAuthManager authManager = new userAuthManager();
                try {
                    userAuth token = authManager.findBySelector(selector);

                    if(token != null) {
                        String hashedValidatorDB = token.getValidator();
                        String hashedValidatorCookie = DigestUtils.sha256Hex(rawvalidator);

                        if (hashedValidatorCookie.equals(hashedValidatorDB)) {
                            UserManager userManager = new UserManager();
                            userBean user = new userBean();
                            user = userManager.selectUserInfoById(token.getUser().getUserNb());
                            Objects.requireNonNull(session).setAttribute("user", user);
                            response.sendRedirect(request.getContextPath() + "/");
                            return;
                        }
                    }

                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        rd.forward(request, response);
    }
}
