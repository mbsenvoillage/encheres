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
        <%@include file="head.jsp"%>
    </head>
    <body>

        <%@include file="header.jsp"%>

        <div class="container">
            <h3 class="text-center">Détail de la vente</h3>

            <c:if test="${article.getSaleStatus() == 'ET' && article.getBid().getBuyerName() == user.getPseudo()}">

                <p class="text-center">Vous avez remporté la vente !</p>

            </c:if>

            <c:if test="${article.getSaleStatus() == 'ET' && article.getSeller().getPseudo() == user.getPseudo()}">

                <p class="text-center">${article.getBid().getBuyerName()} a remporté la vente</p>

            </c:if>

            <div class="row">

                <ul class="list-group">
                    <li class="list-group-item text-center">${article.getArtName()}</li>
                    <li class="list-group-item">Description : ${article.getArtDescrip()}</li>
                    <li class="list-group-item">Catégorie : ${article.getCategory().getCatName()}</li>
                    <li class="list-group-item">Meilleure offre:
                        <c:choose>
                            <c:when test="${article.getSalePrice() == 0}">
                                pas d'offre pour le moment.
                            </c:when>
                            <c:when test="${article.getSaleStatus() == 'ET' && article.getBid().getBuyerName() == user.getPseudo()}">
                                ${article.getSalePrice()}
                            </c:when>
                            <c:otherwise>
                                ${article.getSalePrice()} par <a href="${pageContext.request.contextPath}/profil?pseudo=${article.getBid().getBuyerName()}">${article.getBid().getBuyerName()}</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    <li class="list-group-item">Mise à prix : ${article.getStartPrice()}</li>
                    <li class="list-group-item">Fin de l'enchère : ${article.endAucToLocalDate()}</li>
                    <li class="list-group-item">Retrait : ${article.getPickUp().toString()}</li>
                    <li class="list-group-item">Vendeur : <a href="${pageContext.request.contextPath}/profil?pseudo=${article.getSeller().getPseudo()}">${article.getSeller().getPseudo()}</a></li>
                    <c:if test="${article.getSaleStatus() == 'ET' && article.getBid().getBuyerName() == user.getPseudo()}">

                        <li class="list-group-item">Téléphone : ${article.getSeller().getTelephone()}</li>

                    </c:if>
                </ul>
            </div>
            <div class="form-row" >
                <div class="col-sm-4 offset-sm-4 text-center">
                    <c:if test="${article.getSaleStatus() == 'EC'}">
                        <form action="${pageContext.request.contextPath}/enchere" method="post">
                            <div class="form-group">
                                    <label for="prix">Ma proposition</label>
                                    <input type="number" class="form-control" id="prix" name="bidAmount" step="10" min="${article.getStartPrice()}">
                            </div>
                            <div class="form-group">
                                <button class="btn btn-lg btn-primary btn-block" type="submit">Enchérir</button>
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
            <c:if test="${user.getPseudo() == article.getSeller().getPseudo() && article.getSaleStatus() == 'CR'}">
                <div class="form-row">
                    <div class="col-sm-4 offset-sm-4 text-center">
                        <form method="get" action="${pageContext.request.contextPath}/nouvellevente?modif=true">
                            <input type="hidden" name="modif" value="true">
                            <input type="hidden" name="artnb" value="${article.getArtNb()}">
                            <button class="btn btn-lg btn-primary btn-block" type="submit">Modifier</button>
                        </form>
                    </div>
                </div>
            </c:if>

            <div class="form-row">
                <div class="col-sm-4 offset-sm-4 text-center">
                    <form method="post" action="${pageContext.request.contextPath}/search?searchcrit=achats&categories=toutes&keyword=&status=EC">
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Retour</button>
                    </form>
                </div>
            </div>
            <div class="form-row">
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
            <c:if test="${requestScope.success != null}">
                <div class="form-row">
                    <div class="col-sm-4 offset-sm-4 text-center">
                        <p>${requestScope.success}</p>
                    </div>
                </div>
            </c:if>

        </div>



        <%@include file="footer.jsp"%>
        <%@include file="scripts.jsp"%>


    </body>
</html>

