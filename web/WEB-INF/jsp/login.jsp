<%@ page import="fr.eni.encheres.messages.LecteurMessage" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 28/07/2020
  Time: 12:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>

    <form name="login" action="${pageContext.request.contextPath}/ServletLogin" method="post">
        <label>Identifiant : <input type="text" name="userID"></label>
        <br>
        <label>Mot de passe : <input type="password" name="password"></label>
        <br>
        <%
            List<Integer> errorList = (List<Integer>) request.getAttribute("errorList");
            if (errorList != null)
            {
                for (int code : errorList) {
        %>
        <p><%= LecteurMessage.getErrorMessage(code) %></p>
        <br>
        <%
            }
            }
        %>
        <input type="submit" value="Connexion">
        <label for="rememberme">
            <input type="checkbox" name="rememberme" id="rememberme">
            Se souvenir de moi
        </label>
        <a href="">Mot de passe oublié</a>
    </form>
    <form action="" method="get">
        <input type="button" value="Créer un compte">
    </form>
<c:choose>
    <c:when test="${!empty user}" >
        <p>${user.getUsername()} vous êtes connecté</p>
    </c:when>
    <c:otherwise>
         <% response.se%>
    </c:otherwise>
</c:choose>

</body>
</html>
