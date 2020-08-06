<%@ page import="java.util.List" %>
<%@ page import="fr.eni.encheres.messages.LecteurMessage" %><%--
  Created by IntelliJ IDEA.
  User: yvonmomboisse
  Date: 04/08/2020
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Enchère</title>
    </head>
    <body>
        <h2>Détail de la vente</h2>

        <c:if test="${article.getSaleStatus() == 'ET' && article.getBid().getBuyerName() == user.getPseudo()}">

            <p>Vous avez remporté la vente </p>

        </c:if>

        <c:if test="${article.getSaleStatus() == 'ET' && article.getSeller().getPseudo() == user.getPseudo()}">

            <p>${article.getBid().getBuyerName()} a remporté la vente</p>

        </c:if>

        <p>${article.getArtName()}</p>

        <p>Description : ${article.getArtDescrip()}</p>

        <p>Catégorie : ${article.getCategory().getCatName()}</p>

        <p>Meilleure offre:

            <c:choose>

                <c:when test="${article.getSalePrice() == 0}">

                    pas d'offre pour le moment.

                </c:when>

                <c:when test="${article.getSaleStatus() == 'ET' && article.getBid().getBuyerName() == user.getPseudo()}">

                    ${article.getSalePrice()}

                </c:when>

                <c:otherwise>
            <a href="${pageContext.request.contextPath}/profil?pseudo=${element.getSeller().getPseudo()}">${element.getSeller().getPseudo()}</a>

                ${article.getSalePrice()} par <a href="${pageContext.request.contextPath}/profil?pseudo=${article.getBid().getBuyerName()}">${article.getBid().getBuyerName()}</a></p>

                </c:otherwise>

            </c:choose>

        <p>Mise à prix : ${article.getStartPrice()}</p>

        <p>Fin de l'enchère : ${article.endAucToLocalDate()}</p>

        <p>Retrait : ${article.getPickUp().toString()}</p>

        <p>Vendeur : ${article.getSeller().getPseudo()}</p>

        <c:if test="${article.getSaleStatus() == 'EC'}">

            <form action="${pageContext.request.contextPath}/enchere" method="post">

                <label for="prix">Ma proposition:</label>

                <input type="number" id="prix" name="bidAmount" step="10" min="${article.getSalePrice()}" value="${article.getSalePrice()}"><br><br>

                <input type="submit" value="Enchérir">

            </form>

        </c:if>

        <c:if test="${requestScope.success}">

            <p>${requestScope.success}</p>

        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/search?searchcrit=achats&categories=toutes&keyword=&status=EC">

            <input type="submit" value="Back">

        </form>

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
    </body>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
</html>
