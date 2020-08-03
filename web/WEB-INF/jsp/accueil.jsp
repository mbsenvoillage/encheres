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
                    <a href="${pageContext.request.contextPath}/nouvellevente">Vendre</a>
                </c:otherwise>
            </c:choose>
        </header>
        <h2>Liste des enchères</h2>

        <c:choose>
            <c:when test="${user != null}">
                <div class="search-bar">
                    <h3>Filtres</h3>
                    <form action="${pageContext.request.contextPath}/search" method="post">
                        <input type="text" name="keyword">
                        <br>
                        <label for="categories">Catégorie :
                            <select name="categories">
                                <option value="toutes" selected>Toutes</option>
                                <option  value="Informatique">Informatique</option>
                                <option value="Ameublement">Ameublement</option>
                                <option value="Vêtements">Vêtement</option>
                                <option value="Sport&Loisirs">Sport&Loisirs</option>
                            </select>
                        </label>
                        <br>
                        <label for="achats">Achats<input type="radio" onclick="disableVentes()" name="searchcrit" value="achats" id="achats" ></label>
                        <label>enchères ouvertes <input type="checkbox" name="status" value="EC" id="enchouv"></label>
                        <label for="enchenc">mes enchères en cours <input type="checkbox" name="status" value="EC" id="enchenc"></label>
                        <label for="enchremp">mes enchères remportées <input type="checkbox" name="status" value="ET" id="enchremp"></label>
                        <br><br>
                        <label for="ventes">Mes ventes <input type="radio" onclick="disableAchats()" name="searchcrit" value="ventes" id="ventes"></label>
                        <label for="ventenc">mes ventes en cours <input type="checkbox" name="ventenc" value="ventenc" id="ventenc"></label>
                        <label for="ventnondeb">ventes non débutées <input type="checkbox" name="ventnondeb" value="ventnondev" id="ventnondeb"></label>
                        <label for="ventter">ventes terminées <input type="checkbox" name="ventter" value="ventter" id="ventter"></label>
                        <br><br>
                        <input type="submit" value="Rechercher">
                    </form>
                </div>
                <div class="search-results">
                    <c:choose>
                        <c:when test="${!empty bids}">
                            <c:forEach items="${bids}" var="bid">
                                <p><strong>${bid.getArtForSale().getArtName()}</strong></p>
                                <p>${bid.getArtForSale().getArtDescrip()}</p>
                                <p>Prix : ${bid.getBidAmount()}</p>
                                <p>Fin de l'enchère : ${bid.getArtForSale().endAucToLocalDate()}</p>
                                <p>Vendeur : <a href="${pageContext.request.contextPath}/profil?pseudo=${bid.getSeller().getPseudo()}">${bid.getSeller().getPseudo()}</a></p>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${allArticles}" var="element">

                                <p><strong>${element.getArtName()}</strong></p>
                                <p>Prix : ${element.getStartPrice()} points</p>
                                <p>Fin de l'enchère : ${element.endAucToLocalDate()}</p>
                                <p>Vendeur : <a href="<c:out value="${profil}"/>">${element.getSeller().getPseudo()}</a></p>
                                <br><br>
                                <c:url value="/profil" var="profil">
                                    <c:param name="pseudo" value="${element.getSeller().getPseudo()}"/>
                                </c:url>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${(empty allArticles && empty bids) && search}">
                        <p>Aucun résultat</p>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="search-bar">
                    <h3>Filtres</h3>
                    <form action="${pageContext.request.contextPath}/search" method="post">
                        <input type="text" name="keyword">
                        <br>
                        <label for="categories">Catégorie :
                            <select id="categories" name="categories">
                                <option value="toutes" selected>Toutes</option>
                                <option  value="Informatique">Informatique</option>
                                <option value="Ameublement">Ameublement</option>
                                <option value="Vêtements">Vêtement</option>
                                <option value="Sport&Loisirs">Sport&Loisirs</option>
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
                    <c:if test="${empty allArticles && search}">
                        <p>Aucun résultat</p>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
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
