<%@ page import="fr.eni.encheres.messages.LecteurMessage" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 28/07/2020
  Time: 12:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Se connecter</title>
    <%@include file="head.jsp"%>
</head>
<body class="text-center">

    <div class="container">
        <div class="row">
                <div class="col-sm-4 offset-sm-4 text-center signin">
                    <img class="mb-4" src="${pageContext.request.contextPath}/media/images/green.svg" width="70" height="70" alt="70">
                    <form name="login" action="${pageContext.request.contextPath}/login" method="post">
                    <div class="form-group">
                        <label for="userID">Identifiant</label>
                        <input type="text" class="form-control" name="userID" id="userID" placeholder="Saisissez votre identifiant">
                    </div>
                    <div class="form-group">
                        <label for="password">Mot de passe</label>
                        <input type="password" class="form-control" name="password" id="password" placeholder="Mot de passe">
                    </div>
                    <div class="form-group form-check">
                        <input type="checkbox" class="form-check-input" id="exampleCheck1">
                        <label class="form-check-label" for="exampleCheck1">Se souvenir de moi</label>

                    </div>
                    <div class="form-group">
                        <a href="">Mot de passé oublié</a>
                    </div>
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Connexion</button>
                    </form>
                    <div class="form-group">
                        <form action="${pageContext.request.contextPath}/signup" method="get">
                            <button class="btn btn-lg btn-primary btn-block" type="submit">Créer un compte</button>
                        </form>
                    </div>
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

    <%@include file="footer.jsp"%>

</body>
</html>
