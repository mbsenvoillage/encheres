<%@ page import="fr.eni.encheres.messages.LecteurMessage" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 29/07/2020
  Time: 09:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Signup</title>
        <%@include file="head.jsp"%>
    </head>
    <body>

        <header>
            <%@include file="header.jsp"%>
        </header>


        <div class="container">
            <h3 class="text-center">Inscrivez-vous, c'est gratuit</h3>
            <form action="${pageContext.request.contextPath}/signup" method="post">
                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="pseudo">Pseudo </label>
                        <input class="form-control" type="text" id="pseudo" name="pseudo" >
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6 nom">
                        <label for="nom">Nom </label>
                        <input class="form-control" type="text" id="nom" name="nom" >
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="prenom">Prénom </label>
                        <input class="form-control" type="text" id="prenom" name="prenom" >
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="email">Email </label>
                        <input class="form-control" type="text" id="email" name="email" >
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="telephone">Téléphone </label>
                        <input class="form-control" type="text" id="telephone" name="telephone" >
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="rue">Rue </label>
                        <input class="form-control" type="text" id="rue" name="rue" >
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="cpo">Code postal </label>
                        <input class="form-control" type="text" id="cpo" name="cpo" >
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="ville">Ville </label>
                        <input class="form-control" type="text" id="ville" name="ville" >
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="mdp">Mot de passe</label>
                        <input class="form-control" type="password" id="mdp" name="mdp" >
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6">
                        <label class="col-form-label" for="confirmation">Confirmation </label>
                        <input class="form-control" type="password" id="confirmation" name="confirmation" >
                    </div>
                </div>
                <div class="form-row " >
                    <div class="col-sm-4 offset-sm-4 text-center btn-creer">
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Créer</button>
                    </div>
                </div>
            </form>
            <div class="form-row">
                <div class="col-sm-4 offset-sm-4 text-center btn-supprimer">
                    <form action="${pageContext.request.contextPath}/" method="get">
                        <button class="btn btn-lg btn-primary btn-block" type="submit" >Annuler</button>
                    </form>
                </div>
            </div>
            <div>
                <div class="form-row" >
                    <div class="col-sm-4 offset-sm-4 text-center">
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
                    </div>
                </div>
            </div>
            <small id="emailHelp" class="form-text text-muted">Tous les champs sont obligatoires, sauf le numéro de téléphone.</small>

        </div>
        <%@include file="footer.jsp"%>
    </body>
</html>
