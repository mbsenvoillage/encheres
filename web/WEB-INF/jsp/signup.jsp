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
    </head>
    <body>
        <div class="signup-form">
            <h1>Inscrivez-vous sur Enchères.org, c'est gratuit</h1>
            <form action="${pageContext.request.contextPath}/signup" method="post">
                <label for="pseudo">Pseudo : <input type="text" id="pseudo" name="pseudo"></label>
                <label for="nom">Nom : <input type="text" id="nom" name="nom"></label><br><br>
                <label for="prenom">Prénom : <input type="text" id="prenom" name="prenom"></label>
                <label for="email">Email : <input type="text" id="email" name="email"></label><br><br>
                <label for="telephone">Téléphone : <input type="text" id="telephone" name="telephone"></label>
                <label for="rue">Rue : <input type="text" id="rue" name="rue"></label><br><br>
                <label for="cpo">Code postal : <input type="text" id="cpo" name="cpo"></label>
                <label for="ville">Ville : <input type="text" id="ville" name="ville"></label><br><br>
                <label for="mdp">Mot de passe : <input type="password" id="mdp" name="mdp"></label>
                <label for="confirmation">Confirmation : <input type="password" id="confirmation" name="confirmation"></label><br><br>
                <input type="submit" value="Créer">
                <input type="submit" name="cancelbtn" value="Annuler">
            </form>

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
            <p>Tous les champs sont obligatoires, sauf le numéro de téléphone.</p>
        </div>
    </body>
</html>
