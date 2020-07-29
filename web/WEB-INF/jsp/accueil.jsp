<%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 29/07/2020
  Time: 08:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Accueil</title>
    </head>
    <body>
        <header><a href="${pageContext.request.contextPath}/ServletLogin">S'inscrire - Se connecter</a></header>
        <h2>Liste des enchères</h2>
        <div class="search-bar">
            <h3>Filtres</h3>
            <form action="${pageContext.request.contextPath}/search" method="post">
                <input type="text" name="keyword">
                <br>
                <label for="categories">Catégorie :
                <select id="categories" name="categories">
                    <option value="toutes">Toutes</option>
                    <option  value="informatique">Informatique</option>
                    <option value="ameublement">Ameublement</option>
                    <option value="vetement">Vêtement</option>
                    <option value="sportloisir">Sport&Loisirs</option>
                </select>
                </label>
                <br>
                <input type="submit" value="Rechercher">
            </form>
        </div>
        <div class="search-results">

        </div>

    </body>
</html>
