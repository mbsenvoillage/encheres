<%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 28/07/2020
  Time: 12:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

    <form name="login" action="${pageContext.request.contextPath}/ServletLogin" method="post">
        <label>Identifiant : <input type="text" name="userID"></label>
        <br>
        <label>Mot de passe : <input type="text" name="password"></label>
        <br>
        <input type="submit" value="Connexion">
        <label for="rememberme">
            <input type="checkbox" name="rememberme" id="rememberme">
            Se souvenir de moi
        </label>
        <a href="">Mot de passe oublié</a>
    </form>
    <form action="" method="get">
        <input type="button" value="Créer un compte">
    </form>

</body>
</html>
