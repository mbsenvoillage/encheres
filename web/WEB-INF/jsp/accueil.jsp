<%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 29/07/2020
  Time: 08:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    %>
    <head>
        <title>Accueil</title>
    </head>
    <body>
        <header>
            <c:choose>
                <c:when test="${user != null}">
                    <a href="${pageContext.request.contextPath}/ServletLogout">Logout</a>
                    <a href="${pageContext.request.contextPath}/nouvellevente">Vendre</a>
                    <a href="${pageContext.request.contextPath}/profil">Profil</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/ServletLogin">S'inscrire - Se connecter</a>
                </c:otherwise>
            </c:choose>
        </header>
        <h2>Liste des enchères</h2>
        <div class="search-bar">
            <h3>Filtres</h3>
            <form action="${pageContext.request.contextPath}/search" method="post">
                <input type="text" name="keyword">
                <br>
                <label for="categories">Catégorie :
                <select id="categories" name="categories">
                    <option value="toutes" selected>Toutes</option>
                    <option  value="informatique">Informatique</option>
                    <option value="ameublement">Ameublement</option>
                    <option value="vetement">Vêtement</option>
                    <option value="sportloisir">Sport&Loisirs</option>
                </select>
                </label>
                <br>
                <input type="submit" value="Rechercher">
            </form>
            <c:url value="/profil" var="profil">
                <c:param name="pseudo" value="lolo93130"/>
            </c:url>
            <a href="<c:out value="${profil}"/>">lolo</a>
        </div>
        <div class="search-results">
            <c:forEach items="${allArticles}" var="element">

                <p><strong>${element.getArtName()}</strong></p>
                    <p>Prix : ${element.getStartPrice()} points</p>
                    <p>Fin de l'enchère : ${element.endAucToLocalDate()}</p>
                    <p>Vendeur : ${element.getSeller().getPseudo()}</p>
                <br><br>

            </c:forEach>
        </div>

    </body>
</html>
