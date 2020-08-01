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
                        <label for="article">Article : <input type="text" name="article"></label><br><br>
                        <label for="description">Description : <textarea name="description" cols="20" rows="5"></textarea></label><br><br>
                        <label for="categorie"> Catégorie
                            <select name="categorie" >
                                <option value="informatique">Informatique</option>
                                <option value="ameublement">Ameublement</option>
                                <option value="vetement">Vêtement</option>
                                <option value="sportloisir">Sport&Loisir</option>
                            </select>
                        </label><br><br>
                        <label for="photo">Photo de l'article <button name="photo">UPLOADER</button></label><br><br>
                        <label for="prix">Points:</label>
                        <input type="number" id="prix" name="prix" step="10"><br><br>
                        <label>Début de l'enchère<input type="date" name="salestart"></label><br><br>
                        <label>Fin de l'enchère<input type="date" name="saleend" ></label><br><br>
                        <fieldset>
                            <legend>Retrait</legend>
                            <label for="rue">Rue : <input type="text" name="rue"></label><br><br>
                            <label for="cpo">Code postal : <input type="text" name="cpo"></label><br><br>
                            <label for="ville">Ville : <input type="text" name="ville"></label><br><br>
                        </fieldset>
                        <input type="submit" value="Créer">
                        <input type="submit" name="cancelbtn" value="Annuler">
                        <c:if test="${param.cancelbtn == 'Annuler'}">
                            <c:redirect url="/accueil"/>
                        </c:if>
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
                                <option value="Sport & Loisirs">Sport&Loisir</option>
                            </select>
                        </label><br><br>
                        <label for="photo">Photo de l'article <button id="photo">UPLOADER</button></label><br><br>
                        <label for="prix">Points:</label>
                        <input type="number" id="prix" name="prix" step="10"><br><br>
                        <label>Début de l'enchère<input type="date" id="salestart" name="salestart"></label><br><br>
                        <label>Fin de l'enchère<input type="date" id="saleend" name="saleend" ></label><br><br>
                        <fieldset>
                            <legend>Retrait</legend>
                            <label for="rue">Rue : <input type="text" id="rue" name="rue"></label><br><br>
                            <label for="cpo">Code postal : <input type="text" id="cpo" name="cpo"></label><br><br>
                            <label for="ville">Ville : <input type="text" id="ville" name="ville"></label><br><br>
                        </fieldset>
                        <input type="submit" value="Créer">
                        <input type="submit" name="cancelbtn" value="Annuler">
                        <c:if test="${param.cancelbtn == 'Annuler'}">
                            <c:redirect url="/accueil"/>
                        </c:if>
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
                    </c:otherwise>
                </c:choose>

                </form>
            </div>
        </main>
    </body>
</html>
