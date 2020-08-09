<%@ page import="java.util.List" %>
<%@ page import="fr.eni.encheres.messages.LecteurMessage" %><%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 29/07/2020
  Time: 08:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Profile</title>
    <%@include file="head.jsp" %>
</head>
<body>
    <!-- || requestScope.pseudo == sessionScope.user.getPseudo() condition problématique
    même sans avoir login, cette condition semble être remplie, puisque la page
    affiche l'option logout-->

    <%@include file="header.jsp" %>
    <div class="container">

        <h3 class="text-center">Mon profil</h3>
        <c:choose>
            <c:when test="${visitor == null && update != null}">

                        <form action="${pageContext.request.contextPath}/modifconfirm" method="post">
                            <div class="form-row">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="pseudo">Pseudo </label>
                                    <input class="form-control" type="text" id="pseudo" name="pseudo" value="${user.getPseudo()}">
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6 nom">
                                    <label for="nom">Nom </label>
                                    <input class="form-control" type="text" id="nom" name="nom" value="${user.getNom()}">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="prenom">Prénom </label>
                                    <input class="form-control" type="text" id="prenom" name="prenom" value="${user.getPrenom()}">
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="email">Email </label>
                                    <input class="form-control" type="text" id="email" name="email" value="${user.getEmail()}">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="telephone">Téléphone </label>
                                    <input class="form-control" type="text" id="telephone" name="telephone" value="${user.getTelephone()}">
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="rue">Rue </label>
                                    <input class="form-control" type="text" id="rue" name="rue" value="${user.getRue()}">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="cpo">Code postal </label>
                                    <input class="form-control" type="text" id="cpo" name="cpo" value="${user.getCpo()}">
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="ville">Ville </label>
                                    <input class="form-control" type="text" id="ville" name="ville" value="${user.getVille()}">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="mdp">Mot de passe actuel </label>
                                    <input class="form-control" type="password" id="mdp" name="mdp" value="${user.getMdp()}">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="nouveaumdp">Nouveau mot de passe </label>
                                    <input class="form-control" type="password" id="nouveaumdp" name="nouveaumdp" value="${user.getMdp()}">
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label" for="confirmation">Confirmation </label>
                                    <input class="form-control" type="password" id="confirmation" name="confirmation" value="${user.getMdp()}">
                                </div>
                            </div>
                            <div class="form-row bg-light">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label">Crédit </label>
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <label class="col-form-label">${user.getCredit()} </label>
                                </div>
                            </div>
                            <div class="form-row " >
                                <div class="col-sm-12 col-md-6 col-lg-6 btn-modifier">
                                    <input class="form-control" type="submit" value="Modifier">
                                </div>
                            </div>
                        </form>
                <div class="form-row">
                    <div class="col-sm-12 col-md-6 col-lg-6 btn-supprimer">
                        <form action="${pageContext.request.contextPath}/delete" method="get">
                            <input class="form-control" type="submit" name="deletebtn" value="Supprimer mon compte">
                        </form>
                    </div>
                </div>

            </c:when>
            <c:otherwise>
                <div class="row">
                    <ul class="list-group">
                        <li class="list-group-item">Pseudo : ${profile.getPseudo()}</li>
                        <li class="list-group-item">Nom : ${profile.getNom()}</li>
                        <li class="list-group-item">Prenom : ${profile.getPrenom()}</li>
                        <li class="list-group-item">Email : ${profile.getEmail()}</li>
                        <li class="list-group-item">Telephone : ${profile.getTelephone()}</li>
                        <li class="list-group-item">Rue : ${profile.getRue()}</li>
                        <li class="list-group-item">Code postal : ${profile.getCpo()}</li>
                        <li class="list-group-item">Ville : ${profile.getVille()}</li>
                    </ul>
                </div>
                <div class="form-row">
                    <c:if test="${visitor == null}">
                        <div class="col-sm-4 offset-sm-4 ">
                            <form id="modifprofil" method="get" action="${pageContext.request.contextPath}/profileupdate">
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Modifier">
                            </form>
                        </div>
                        <div class="col-sm-4 offset-sm-4">
                            <c:if test="${!empty requestScope.success}">
                                <p>${requestScope.success}</p>
                            </c:if>
                        </div>
                    </c:if>
                </div>
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
            </c:otherwise>
        </c:choose>
    </div>

    <%@include file="footer.jsp"%>
    <%@include file="scripts.jsp"%>

</body>
</html>
