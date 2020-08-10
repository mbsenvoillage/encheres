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

                <form id="saleform"  action="${pageContext.request.contextPath}/nouvellevente<c:if test="${added || modified || failedmodif}">?modif=true&status=${article.getSaleStatus()}&artnb=${article.getArtNb()}</c:if>" method="post">
                    <div class="form-group">
                        <label for="article">Article</label>
                        <input type="text" name="article" class="form-control" id="article" <c:if test="${added || modified || failedmodif}">value="${article.getArtName()}"</c:if>>
                    </div>
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="3" ><c:if test="${added || modified || failedmodif}">${article.getArtDescrip()}</c:if></textarea>
                    </div>
                    <div class="form-group">
                        <label for="categorie"> Catégorie</label>
                        <select class="form-control" name="categorie" id="categorie">
                            <option value="Informatique" <c:if test="${added || modified || failedmodif}"><c:if test="${article.getCategory().getCatName() == 'Informatique'}">selected</c:if></c:if> >Informatique</option>

                            <option value="Ameublement" <c:if test="${added || modified || failedmodif}"><c:if test="${article.getCategory().getCatName() == 'Ameublement'}">selected</c:if></c:if> >Ameublement</option>

                            <option value="Vêtements" <c:if test="${added || modified || failedmodif}"><c:if test="${article.getCategory().getCatName() == 'Vêtements'}">selected</c:if></c:if> >Vêtement</option>

                            <option value="Sport&Loisirs" <c:if test="${added || modified || failedmodif}"><c:if test="${article.getCategory().getCatName() == 'Sport&Loisirs'}">selected</c:if></c:if> >Sport&Loisir</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="col-form-label" for="photo">Photo de l'article </label>
                        <input class="form-control-file" type="file" id="photo" name="photo">
                    </div>
                    <div class="form-group">
                        <label for="prix">Points:</label>
                        <input class="form-control" type="number" id="prix" name="prix" step="10" min="0" <c:if test="${added || modified || failedmodif}">value="${article.getStartPrice()}"</c:if> >

                    </div>
                    <div class="form-group">
                        <label for="salestart">Début de l'enchère</label>
                        <input class="form-control" type="datetime-local" id="salestart" name="salestart" <c:if test="${added || modified || failedmodif}">value="${article.getStartAuc()}"</c:if>>
                    </div>
                    <div class="form-group">
                        <label for="saleend">Fin de l'enchère</label>
                        <input class="form-control" type="datetime-local" name="saleend" id="saleend" <c:if test="${added || modified || failedmodif}">value="${article.getEndAuc()}"</c:if>>
                    </div>
                    <fieldset class="form-group">
                        <legend>Retrait</legend>
                        <div class="form-group">
                            <label for="rue">Rue</label>
                            <input class="form-control" id="rue" type="text" name="rue" value="${user.getRue()}" <c:if test="${added || modified || failedmodif}">value="${article.getPickUp().getRue()}"</c:if>>
                        </div>
                        <div class="form-group">
                            <label for="cpo">Code postal</label>
                            <input class="form-control" id="cpo" type="text" name="cpo" value="${user.getCpo()}" <c:if test="${added || modified || failedmodif}">value="${article.getPickUp().getCpo()}"</c:if>>
                        </div>
                        <div class="form-group">
                            <label for="ville">Ville</label>
                            <input class="form-control" id="ville" type="text" name="ville" value="${user.getVille()}" <c:if test="${added || modified || failedmodif}">value="${article.getPickUp().getVille()}"</c:if>>
                        </div>
                    </fieldset>
                    <div class="form-row " >
                        <div class="col-sm-12 col-md-6 col-lg-6 btn-modifier">
                            <c:choose>
                                <c:when test="${added || modified || failedmodif}">
                                    <input class="form-control btn btn-lg btn-primary btn-block" type="submit" value="Modifier">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control btn btn-lg btn-primary btn-block" type="submit" value="Enregistrer">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </form>

                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6 btn-supprimer">
                        <form action="${pageContext.request.contextPath}/" method="get">
                            <input class="form-control btn btn-lg btn-primary btn-block" type="submit" name="deletebtn" value="Annuler">
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
                    <c:if test="${!tobemodified}">
                        <p class="text-center">L'annonce a bien été ajoutée.</p>
                    </c:if>
                </c:if>
                <div class="col-sm-4 offset-sm-4 text-center signin">
                    <c:if test="${modified}">
                        <p class="text_center">L'annonce a bien été modifiée</p>
                    </c:if>
                </div>


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
