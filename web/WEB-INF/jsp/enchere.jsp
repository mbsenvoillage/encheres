<%--
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
        <title>Faire une enchère</title>
    </head>
    <body>
        <h2>Détail de la vente</h2>
        <p>${article.getArtName()}</p>
        <p>Description : ${article.getArtDescrip()}</p>
        <p>Catégorie : ${article.getCategory().getCatName()}</p>
        <p>Meilleure offre: ${article.getSalePrice()} par ${article.getBid().getBuyerName()}</p>
        <p>Mise à prix : ${article.getStartPrice()}</p>
        <p>Fin de l'enchère : ${article.endAucToLocalDate()}</p>
        <p>Retrait : ${article.getPickUp().toString()}</p>
        <p>Vendeur : ${article.getSeller().getPseudo()}</p>
        <form>
            <label for="prix">Points:</label>
            <input type="number" id="prix" name="prix" step="10" min="${article.getSalePrice()}" value="${article.getSalePrice()}"><br><br>
            <input type="submit" value="Enchérir">
        </form>

    </body>
</html>
