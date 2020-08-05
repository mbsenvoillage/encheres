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
                    <a href="${pageContext.request.contextPath}/nouvellevente">Vendre</a>
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
                    <select name="categories" id="categories">
                        <option value="toutes" selected>Toutes</option>
                        <option  value="Informatique">Informatique</option>
                        <option value="Ameublement">Ameublement</option>
                        <option value="Vêtements">Vêtement</option>
                        <option value="Sport&Loisirs">Sport&Loisirs</option>
                    </select>
                </label>
                <br>
                <c:if test="${user != null}">
                    <label for="achats">Achats<input type="radio" onclick="disableVentes()" name="searchcrit" value="achats" id="achats" checked></label>
                    <label>enchères ouvertes <input type="checkbox" name="status" value="EC" id="enchouv"></label>
                    <label for="enchenc">mes enchères en cours <input type="checkbox" name="status" value="MEC" id="enchenc"></label>
                    <label for="enchremp">mes enchères remportées <input type="checkbox" name="status" value="MER" id="enchremp"></label>
                    <br><br>
                    <label for="ventes">Mes ventes <input type="radio" onclick="disableAchats()" name="searchcrit" value="ventes" id="ventes"></label>
                    <label for="ventenc">mes ventes en cours <input type="checkbox" name="status" value="EC" id="ventenc"></label>
                    <label for="ventnondeb">ventes non débutées <input type="checkbox" name="status" value="CR" id="ventnondeb"></label>
                    <label for="ventter">ventes terminées <input type="checkbox" name="status" value="ET" id="ventter"></label>
                    <br><br>
                </c:if>
                <input type="submit" value="Rechercher">
            </form>
        </div>
        <div class="search-results">
            <c:forEach items="${allArticles}" var="element">
                <p><strong><a <c:if test="${user != null}">href="${pageContext.request.contextPath}/enchere?article=${element.getArtName()}"</c:if>>${element.getArtName()}</a></strong></p>
            <p>Prix : ${element.getStartPrice()} points</p>
            <p>Fin de l'enchère : ${element.endAucToLocalDate()}</p>
            <p>Vendeur : Vendeur : <a href="${pageContext.request.contextPath}/profil?pseudo=${element.getSeller().getPseudo()}">${element.getSeller().getPseudo()}</a></p>
            <br><br>
            </c:forEach>
            <c:if test="${(empty allArticles) && search}">
                <p>Aucun résultat</p>
            </c:if>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
        <script>
            function disableVentes() {
                document.getElementById("ventenc").disabled = true;
                document.getElementById("ventnondeb").disabled = true;
                document.getElementById("ventter").disabled = true;
                document.getElementById("enchouv").disabled = false;
                document.getElementById("enchenc").disabled = false;
                document.getElementById("enchremp").disabled = false;
            }

            function disableAchats() {
                document.getElementById("enchouv").disabled = true;
                document.getElementById("enchenc").disabled = true;
                document.getElementById("enchremp").disabled = true;
                document.getElementById("ventenc").disabled = false;
                document.getElementById("ventnondeb").disabled = false;
                document.getElementById("ventter").disabled = false;
            }

            $(document).ready(function()
            {
                $('#ventenc').prop('disabled', true);
                $('#ventnondeb').prop('disabled', true);
                $('#ventter').prop('disabled', true);
            });
        </script>

    </body>
</html>
