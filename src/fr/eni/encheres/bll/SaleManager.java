package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.*;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.SaleDAO;
import fr.eni.encheres.dal.UserDAO;

import java.awt.geom.PathIterator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class SaleManager {

    private SaleDAO articleforSale;

    public SaleManager() { this.articleforSale = DAOFactory.getSaleDAO(); }

    // PERMET DE METTRE A JOUR LE STATUT DE VENTE

    public void updateSaleStatus() throws BusinessException {
        articleforSale.updateSaleStatus();
    }

    // PERMET D'AFFICHER LE DETAIL D'UNE ENCHERE

    public articleBean auctionDetail(String artName) throws BusinessException {
        articleBean article = new articleBean();
        article = articleforSale.detailAuction(artName);

        if (articleforSale.selectHighestBidder(artName) != null) {
            article.setBid(articleforSale.selectHighestBidder(artName));
        }
        return article;
    }

    // PERMET DE CONNAITRE LE BALANCE DES COMPTES DE L'UTILISATEUR

    public int getAccountBalance(int userNb) throws BusinessException {
        return articleforSale.checkUserCredit(userNb);
    }

    // PERMET DE METTRE A JOUR LE PRIX DE VENTE D'UN ARTICLE APRES NOUVELLE ENCHERE

    public void updateSalePrice(int price, int artNb) throws BusinessException {
        articleforSale.updateArtSalePrice(price, artNb);
    }

    // PERMET D'AJOUTER UNE NOUVELLE ENCHERE

    public biddingBean placeBid(biddingBean bid) throws BusinessException {
        return articleforSale.insertBid(bid);
    }

    // PERMET D'AFFICHER LES VENTES DE L'UTILISATEUR

    public List<articleBean> displayUserArticlesForSale(Integer userNb, String name, String cat, String status) throws BusinessException {
        return articleforSale.selectUserItemsForSale(userNb, name, cat, status);
    }

    // PERMET D'AFFICHER LES ARTICLES EN VENTE (selon le nom, la catégorie et le statut (CR, EC, ET))

    public List<articleBean> displayAllArticles(String name, String cat, String status) throws BusinessException {
        return articleforSale.selectAllArticles(name, cat, status);
    }

    // PERMET D'AFFICHER LES ENCHERES (selon l'enchérisseur, le nom, la catégorie et le statut)

    public List<articleBean> displayAuctions(int userNb, String name, String cat, String status) throws BusinessException {
        return articleforSale.selectOngoingAuctions(userNb, name, cat, status);
    }

    // PERMET D'AFFICHER LES ENCHERES REMPORTEES PAR L'UTILISATEUR

    public List<articleBean> displayUserWiningBids(Integer userNb, String name, String cat) throws BusinessException {
        return articleforSale.selectUserWinningBids(userNb, name, cat);
    }

    // PERMET DE METTRE A JOUR UN ARTICLE EN VENTE
    public articleBean addArticleForSale(Map parametres, articleBean article, userBean user) throws BusinessException {
        BusinessException bizEx = new BusinessException();

        // Tous les champs ont-ils été remplis ?

        this.allFieldsAreFilled(parametres, bizEx);

        // Les dates sont-elles valides d'un point de vue logique ?

        this.dateIsValid(article.getStartAuc(), article.getEndAuc(), bizEx);

        // On affecte au champ no_categorie la valeur correspondante

        article.getCategory().setCatNb(getCatId(article.getCategory().getCatName()));
        System.out.println("THIS is the cat nb" + article.getCategory().getCatNb());

        // Si les tests ci-dessus n'ont levé aucune erreur, on procède à l'ajout de l'annonce

        if (!bizEx.containsErrors()) {
            articleforSale.inserArticle(article, user);
        } else {
            throw bizEx;
        }

        return article;
    }

    // PERMET D'AJOUTER UNE ADDRESSE DE RETRAIT SUITE A UN DEPOS D'ANNONCE

    public void addPickUpAddress(PickUp retrait, int artNb) throws BusinessException {
        articleforSale.insertRetrait(retrait, artNb);
    }

    /*
        Cette méthode s'assure que la date de début des enchères est postérieure à la date d'aujourd'hui
        et que la date de fin est postérieure à celle de début
     */

    private void dateIsValid(LocalDateTime startdate, LocalDateTime endDate, BusinessException bizex) {
        if (startdate == null || endDate == null || startdate.isBefore(LocalDateTime.now().minusMinutes(10)) || endDate.isBefore(startdate)) {
            bizex.addError(CodesErreurBLL.ERREUR_DATE);
        }
    }

    // Cette méthode s'assure que tous les champs nécessaires à l'ajout d'un article sont remplis

    private void allFieldsAreFilled(Map parametres, BusinessException biz) {
        for (Object o : parametres.keySet()) {
            String key = (String) o;
            String value = ((String[]) parametres.get(key))[0];
            // Si un champ n'est pas renseigné et que ce n'est pas le champ téléphone alors
            // l'utilisateur n'a pas rempli tous les champs
            if (value.trim().isEmpty() && !key.equals("photo")) {
                biz.addError(CodesErreurBLL.CHAMPS_VIDE_ERREUR);
                break;
            }
        }
    }

    public int getCatId(String cat) throws BusinessException {
        return articleforSale.selectCatByName(cat);
    }

}

// TODO move some methods in a utility class (ex: allFieldsAreFilled)