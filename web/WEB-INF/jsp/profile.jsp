<%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 29/07/2020
  Time: 08:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile</title>
</head>
<body>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
    <p>Bonjour ${user.getUsername()}</p>
    <a href="${pageContext.request.contextPath}/ServletLogout">Logout</a>

</body>
</html>
