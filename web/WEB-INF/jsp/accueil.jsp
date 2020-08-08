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



<!DOCTYPE html>
<html>
    <head>
        <title>Accueil</title>
        <%@include file="head.jsp"%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/css/main.css" type="text/css">
        <script src="${pageContext.request.contextPath}/styles/js/button.js"></script>
    </head>

    <body>

        <%@include file="header.jsp"%>

        <div class="container">

            <h3 class="text-center">Liste des enchères</h3>

            <div class="container">
                <h4>Filtres</h4>
                <form action="${pageContext.request.contextPath}/search" method="post">
                <div class="form-row">
                    <div class="form-group col-sm-12 col-md-4">
                        <input type="text" name="keyword" class="form-control" placeholder="Le nom de l'article contient">
                    </div>
                    <div class="form-group col-sm-12 col-md-4">
                        <select name="categories" id="categories" class="form-control">
                            <option value="toutes" >Catégories</option>
                            <option value="toutes">Toutes</option>
                            <option  value="Informatique">Informatique</option>
                            <option value="Ameublement">Ameublement</option>
                            <option value="Vêtements">Vêtement</option>
                            <option value="Sport&Loisirs">Sport&Loisirs</option>
                        </select>
                    </div>
                    <div class="form-group col-sm-12 col-md-4">
                        <input type="submit" value="Rechercher" class="btn btn-primary col-12">
                    </div>
                </div>
                <c:if test="${user != null}">
                <div class="form-row">

                    <div class="form-group col-sm-12 col-md-6 col-lg-6">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" onclick="disableVentes()" name="searchcrit" value="achats" id="achats" checked>
                            <label for="achats" class="form-check-label">Achats</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="status" value="EC" id="enchouv">
                            <label class="form-check-label" for="enchouv">
                                enchères ouvertes
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="status" value="MEC" id="enchenc">
                            <label class="form-check-label" for="enchenc">
                                mes enchères en cours
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="status" value="MER" id="enchremp">
                            <label class="form-check-label" for="enchremp">
                                mes enchères remportées
                            </label>
                        </div>

                    </div>
                    <div class="form-group col-sm-12 col-md-6 col-lg-6">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" onclick="disableAchats()" name="searchcrit" value="ventes" id="ventes">
                            <label for="ventes" class="form-check-label">Mes ventes</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="status" value="EC" id="ventenc">
                            <label class="form-check-label" for="ventenc">
                                mes ventes en cours
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="status" value="CR" id="ventnondeb">
                            <label class="form-check-label" for="ventnondeb">
                                ventes non débutées
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="status" value="ET" id="ventter">
                            <label class="form-check-label" for="ventter">
                                ventes terminées
                            </label>
                        </div>
                    </div>
                </div>
                </c:if>

                </form>
                <div class="row">
                    <c:forEach items="${allArticles}" var="element">
                        <div class="col-sm-12 col-md-6 col-lg-6 ">
                            <div class="card col-12" style="width: 20rem;">
                                <div class="card-body">
                                    <h5 class="card-title"><a <c:if test="${user != null}">href="${pageContext.request.contextPath}/enchere?artNb=${element.getArtNb()}"</c:if>>${element.getArtName()}</a></h5>
                                    <p>Prix : ${element.getStartPrice()} points</p>
                                    <p>Fin de l'enchère : ${element.endAucToLocalDate()}</p>
                                    <p>Vendeur : </p>
                                    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/profil?pseudo=${element.getSeller().getPseudo()}">${element.getSeller().getPseudo()}</a></p>
                                </div>
                            </div>

                            <br><br>
                        </div>

                    </c:forEach>
                    <c:if test="${(empty allArticles) && search}">
                        <p>Aucun résultat</p>
                    </c:if>
                </div>
            </div>
        </div>

        <%@include file="footer.jsp"%>
        <%@include file="scripts.jsp"%>
    </body>
</html>
