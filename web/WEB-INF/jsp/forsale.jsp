<%@ page import="java.util.List" %>
<%@ page import="fr.eni.encheres.messages.LecteurMessage" %><%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 31/07/2020
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Nouvelle Vente</title>
    </head>
    <body>
        <header><h1>Nouvelle vente</h1></header>
        <main>
            <div>
                <c:choose>
                    <c:when test="${added}">
                    <form id="saleform" action="${pageContext.request.contextPath}/nouvellevente" method="post">
                        <label for="article">Article : <input type="text" name="article" value="${article.getArtName()}"></label><br><br>
                        <label for="description">Description : <textarea name="description" cols="20" rows="5" >${article.getArtDescrip()}</textarea></label><br><br>
                        <label for="categorie"> Catégorie
                            <select name="categorie" >
                                <option value="Informatique" <c:if test="${article.getArtName() == 'Informatique'}">selected</c:if> >Informatique</option>
                                <option value="Ameublement" <c:if test="${article.getArtName() == 'Ameublement'}">selected</c:if>>Ameublement</option>
                                <option value="Vêtements" <c:if test="${article.getArtName() == 'Vetements'}">selected</c:if>>Vêtement</option>
                                <option value="Sport&Loisirs" <c:if test="${article.getArtName() == 'Sport & Loisirs'}">selected</c:if>>Sport&Loisir</option>
                            </select>
                        </label><br><br>
                        <label for="photo">Photo de l'article <button name="photo">UPLOADER</button></label><br><br>
                        <label for="prix">Points:</label>
                        <input type="number" id="prix" name="prix" step="10" min="0" value="${article.getStartPrice()}"><br><br>
                        <label>Début de l'enchère<input type="datetime-local" name="salestart"></label><br><br>
                        <label>Fin de l'enchère<input type="datetime-local" name="saleend" ></label><br><br>
                        <fieldset>
                            <legend>Retrait</legend>
                            <label for="rue">Rue : <input type="text" name="rue" value="${article.getPickUp().getRue()}"></label><br><br>
                            <label for="cpo">Code postal : <input type="text" name="cpo" value="${article.getPickUp().getCpo()}"></label><br><br>
                            <label for="ville">Ville : <input type="text" name="ville" value="${article.getPickUp().getVille()}"></label><br><br>
                        </fieldset>
                        <input type="submit" value="Enregistrer">
                    </form>
                    <form action="${pageContext.request.contextPath}/" method="get">
                        <input type="submit" name="cancelbtn" value="Annuler">
                    </form>
                    <form action="${pageContext.request.contextPath}/" method="get">
                        <input type="submit" name="cancelbtn" value="Annuler la vente">
                    </form>
                    <p>L'annonce a bien été ajoutée.</p>
                    </c:when>
                    <c:otherwise>
                    <form id="saleform" action="${pageContext.request.contextPath}/nouvellevente" method="post">
                        <label for="article">Article : <input type="text" id="article" name="article"></label><br><br>
                        <label for="description">Description : <textarea id="description" name="description" cols="20" rows="5"></textarea></label><br><br>
                        <label for="categorie"> Catégorie
                            <select name="categorie" id="categorie">
                                <option value="Informatique">Informatique</option>
                                <option value="Ameublement">Ameublement</option>
                                <option value="Vetements">Vêtement</option>
                                <option value="Sport&Loisirs">Sport&Loisirs</option>
                            </select>
                        </label><br><br>
                        <label for="photo">Photo de l'article <button id="photo">UPLOADER</button></label><br><br>
                        <label for="prix">Points:</label>
                        <input type="number" name="prix" step="10"><br><br>
                        <label>Début de l'enchère<input type="datetime-local" id="salestart" name="salestart"></label><br><br>
                        <label>Fin de l'enchère<input type="datetime-local" id="saleend" name="saleend" ></label><br><br>
                        <fieldset>
                            <legend>Retrait</legend>
                            <label for="rue">Rue : <input type="text" id="rue" name="rue" value="${user.getRue()}"></label><br><br>
                            <label for="cpo">Code postal : <input type="text" id="cpo" name="cpo" value="${user.getVille()}"></label><br><br>
                            <label for="ville">Ville : <input type="text" id="ville" name="ville" value="${user.getCpo()}"></label><br><br>
                        </fieldset>
                        <input type="submit" value="Enregistrer">
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
                    </form>
                        <form action="${pageContext.request.contextPath}/" method="get">
                            <input type="submit" name="cancelbtn" value="Annuler">
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </body>
</html>
