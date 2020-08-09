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

<!DOCTYPE html>
<html>
    <head>
        <title>Nouvelle Vente</title>
        <%@include file="head.jsp"%>
    </head>
    <body>

        <header>
            <%@include file="header.jsp"%>
        </header>

        <main>

            <div class="container">
                <h3 class="text-center">Nouvelle vente</h3>

                <form id="saleform" action="${pageContext.request.contextPath}/nouvellevente" method="post">
                    <div class="form-group">
                        <label for="article">Article</label>
                        <input type="text" name="article" class="form-control" id="article" <c:if test="${added}">value="${article.getArtName()}"</c:if>>
                    </div>
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="3" ><c:if test="${added}">${article.getArtDescrip()}</c:if></textarea>
                    </div>
                    <div class="form-group">
                        <label for="categorie"> Catégorie</label>
                        <select class="form-control" name="categorie" id="categorie">
                            <option value="Informatique" <c:if test="${added}"><c:if test="${article.getArtName() == 'Informatique'}">selected</c:if></c:if> >Informatique</option>

                            <option value="Ameublement" <c:if test="${added}"><c:if test="${article.getArtName() == 'Ameublement'}">selected</c:if></c:if> >Ameublement</option>

                            <option value="Vêtements" <c:if test="${added}"><c:if test="${article.getArtName() == 'Vêtements'}">selected</c:if></c:if> >Vêtement</option>

                            <option value="Sport&Loisirs" <c:if test="${added}"><c:if test="${article.getArtName() == 'Sport&Loisirs'}">selected</c:if></c:if> >Sport&Loisir</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="col-form-label" for="photo">Photo de l'article </label>
                        <input class="form-control-file" type="file" id="photo" name="photo">
                    </div>
                    <div class="form-group">
                        <label for="prix">Points:</label>
                        <input class="form-control" type="number" id="prix" name="prix" step="10" min="0" <c:if test="${added}">value="${article.getStartPrice()}"</c:if> >

                    </div>
                    <div class="form-group">
                        <label for="salestart">Début de l'enchère</label>
                        <input class="form-control" type="datetime-local" id="salestart" name="salestart" <c:if test="${added}">value="${article.getStartAuc()}"</c:if>>
                    </div>
                    <div class="form-group">
                        <label for="saleend">Fin de l'enchère</label>
                        <input class="form-control" type="datetime-local" name="saleend" id="saleend" <c:if test="${added}">value="${article.getEndAuc()}"</c:if>>
                    </div>
                    <fieldset class="form-group">
                        <legend>Retrait</legend>
                        <div class="form-group">
                            <label for="rue">Rue</label>
                            <input class="form-control" id="rue" type="text" name="rue" value="${user.getRue()}" <c:if test="${added}">value="${article.getPickUp().getRue()}"</c:if>>
                        </div>
                        <div class="form-group">
                            <label for="cpo">Code postal</label>
                            <input class="form-control" id="cpo" type="text" name="cpo" value="${user.getCpo()}" <c:if test="${added}">value="${article.getPickUp().getCpo()}"</c:if>>
                        </div>
                        <div class="form-group">
                            <label for="ville">Ville</label>
                            <input class="form-control" id="ville" type="text" name="ville" value="${user.getVille()}" <c:if test="${added}">value="${article.getPickUp().getVille()}"</c:if>>
                        </div>
                    </fieldset>
                    <div class="form-row " >
                        <div class="col-sm-12 col-md-6 col-lg-6 btn-modifier">
                            <input class="form-control" type="submit" value="Enregistrer">
                        </div>
                    </div>
                </form>

                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6 btn-supprimer">
                        <form action="${pageContext.request.contextPath}/" method="get">
                            <input class="form-control" type="submit" name="deletebtn" value="Annuler">
                        </form>
                    </div>
                </div>
                <c:if test="${added}">
                    <div class="form-row">
                        <div class="col-sm-12 col-md-6 col-lg-6 btn-supprimer">
                            <form action="${pageContext.request.contextPath}/" method="get">
                                <input class="form-control" type="submit" name="deletebtn" value="Annuler la vente">
                            </form>
                        </div>
                    </div>

                    <p class="text-center">L'annonce a bien été ajoutée.</p>

                </c:if>

                <%
                    List<Integer> errorList = (List<Integer>) request.getAttribute("errorList");
                    if (errorList != null)
                    {
                        for (int code : errorList) {
                %>
                <p class="text-center"><%= LecteurMessage.getErrorMessage(code) %></p>
                <br>
                <%
                        }
                    }
                %>

            </div>
        </main>
        <%@include file="footer.jsp"%>
        <%@include file="scripts.jsp"%>
    </body>
</html>
