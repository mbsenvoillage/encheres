<%--
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
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
    <header>
        <c:choose>
            <c:when test="${!empty user}" >
                <c:if test="${param.pseudo == user.getPseudo()}">
                    <a href="${pageContext.request.contextPath}/ServletLogout">Logout</a>
                </c:if>
            </c:when>
            <c:otherwise>
                <p>Ceci est le profil de ${param.pseudo}</p>
                <a href="${pageContext.request.contextPath}/">Accueil</a>
            </c:otherwise>
        </c:choose>
    </header>
    <div class="profilecard">
        <ul>
            <li>Pseudo : ${profilepublicinfo.getPseudo()}</li>
            <li>Nom : ${profilepublicinfo.getNom()}</li>
            <li>Prenom : ${profilepublicinfo.getPrenom()}</li>
            <li>Email : ${profilepublicinfo.getEmail()}</li>
            <li>Telephone : ${profilepublicinfo.getTelephone()}</li>
            <li>Rue : ${profilepublicinfo.getRue()}</li>
            <li>Code postal : ${profilepublicinfo.getCpo()}</li>
            <li>Ville : ${profilepublicinfo.getVille()}</li>
        </ul>

        <form hidden id="profile-display">
            <label for="pseudo">Pseudo : <input type="text" id="pseudo" name="pseudo" value="${profilepublicinfo.getPseudo()}"></label><br><br>
            <label for="nom">Nom : <input type="text" id="nom" name="nom"></label><br><br>
            <label for="prenom">Prénom : <input type="text" id="prenom" name="prenom" value="${profilepublicinfo.getPrenom()}"></label><br><br>
            <label for="email">Email : <input type="text" id="email" name="email" value="${profilepublicinfo.getEmail()}"></label><br><br>
            <label for="telephone">Téléphone : <input type="text" id="telephone" name="telephone" value="${profilepublicinfo.getTelephone()}"></label><br><br>
            <label for="rue">Rue : <input type="text" id="rue" name="rue" value="${profilepublicinfo.getRue()}"></label><br><br>
            <label for="cpo">Code postal : <input type="text" id="cpo" name="cpo" value="${profilepublicinfo.getCpo()}"></label><br><br>
            <label for="ville">Ville : <input type="text" id="ville" name="ville" value="${profilepublicinfo.getVille()}"></label><br><br>
            <br><br>
            <c:choose>
                <c:when test="${!empty user}" >
                    <c:if test="${param.pseudo == user.getPseudo()}">
                        <input type="submit" value="Créer">
                    </c:if>
                </c:when>
            </c:choose>
        </form>
    </div>


</body>
</html>
