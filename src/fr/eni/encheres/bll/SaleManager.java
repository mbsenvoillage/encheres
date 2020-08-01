package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Category;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.SaleDAO;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class SaleManager {
    private SaleDAO articleforSale;

    public SaleManager() { this.articleforSale = DAOFactory.getSaleDAO(); }


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

    /*
        Cette méthode s'assure que la date de début des enchères est postérieure à la date d'aujourd'hui
        et que la date de fin est postérieure à celle de début
     */

    private void dateIsValid(LocalDateTime startdate, LocalDateTime endDate, BusinessException bizex) {
        if (startdate == null || endDate == null || startdate.isBefore(LocalDateTime.now()) || endDate.isBefore(startdate)) {
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