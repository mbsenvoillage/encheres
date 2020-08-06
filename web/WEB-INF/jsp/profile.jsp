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
</head>
<body>
    <!-- || requestScope.pseudo == sessionScope.user.getPseudo() condition problématique
    même sans avoir login, cette condition semble être remplie, puisque la page
    affiche l'option logout-->
    <header>
        <c:choose>
            <c:when test="${visitor == null}" >
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
                <a href="${pageContext.request.contextPath}/">Accueil</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/">Accueil</a>
            </c:otherwise>
        </c:choose>
    </header>
    <div class="profilecard">
        <c:choose>
            <c:when test="${visitor == null && update != null}">

                        <form action="${pageContext.request.contextPath}/modifconfirm" method="post">
                            <label for="pseudo">Pseudo : <input type="text" id="pseudo" name="pseudo" value="${user.getPseudo()}"></label><br><br>
                            <label for="nom">Nom : <input type="text" id="nom" name="nom" value="${user.getNom()}"></label><br><br>
                            <label for="prenom">Prénom : <input type="text" id="prenom" name="prenom" value="${user.getPrenom()}"></label><br><br>
                            <label for="email">Email : <input type="text" id="email" name="email" value="${user.getEmail()}"></label><br><br>
                            <label for="telephone">Téléphone : <input type="text" id="telephone" name="telephone" value="${user.getTelephone()}"></label><br><br>
                            <label for="rue">Rue : <input type="text" id="rue" name="rue" value="${user.getRue()}"></label><br><br>
                            <label for="cpo">Code postal : <input type="text" id="cpo" name="cpo" value="${user.getCpo()}"></label><br><br>
                            <label for="ville">Ville : <input type="text" id="ville" name="ville" value="${user.getVille()}"></label><br><br>
                            <label for="mdp">Ancien mot de passe : <input type="password" id="mdp" name="mdp" value="${user.getMdp()}"></label>
                            <label for="nouveaumdp">Nouveau mot de passe : <input type="password" id="nouveaumdp" name="nouveaumdp" value="${user.getMdp()}"></label>
                            <label for="confirmation">Confirmation : <input type="password" id="confirmation" name="confirmation" value="${user.getMdp()}"></label><br><br>
                            <br><br>
                            <input type="submit" value="Modifier">
                        </form>
                        <form action="${pageContext.request.contextPath}/delete" method="get">
                            <input type="submit" name="deletebtn" value="Supprimer mon compte">
                        </form>
            </c:when>
            <c:otherwise>
                <ul>
                    <li>Pseudo : ${profile.getPseudo()}</li>
                    <li>Nom : ${profile.getNom()}</li>
                    <li>Prenom : ${profile.getPrenom()}</li>
                    <li>Email : ${profile.getEmail()}</li>
                    <li>Telephone : ${profile.getTelephone()}</li>
                    <li>Rue : ${profile.getRue()}</li>
                    <li>Code postal : ${profile.getCpo()}</li>
                    <li>Ville : ${profile.getVille()}</li>
                </ul>
                <c:if test="${visitor == null}">
                    <form id="modifprofil" method="get" action="${pageContext.request.contextPath}/profileupdate">
                        <input type="submit" value="Modifier">
                    </form>
                    <c:if test="${!empty requestScope.success}">
                        <p>${requestScope.success}</p>
                    </c:if>
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
    </div>
</body>
</html>
