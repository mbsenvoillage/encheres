<%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 28/07/2020
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Success</title>
</head>
<body>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
<p>${user.getUsername()} la connexion est réussie</p>
<p>${user.isConnecte()}</p>
<p><%= request.getAttribute("userID")%></p>
<a href="${pageContext.request.contextPath}/ServletLogout">Logout</a>
<a href="${pageContext.request.contextPath}/ServletLogin">Login page</a>
</body>
</html>
