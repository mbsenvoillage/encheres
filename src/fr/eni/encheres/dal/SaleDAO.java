package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Category;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.userBean;

import java.util.List;

public interface SaleDAO {

    public articleBean inserArticle(articleBean article, userBean user) throws BusinessException;

    public int selectCatByName(String cat) throws BusinessException;

    public List<articleBean> selectAllArticles() throws BusinessException;

    public List<articleBean> selectArticlesByName(String name) throws BusinessException;

    public List<articleBean> selectArticlesByNameAndCat(String name, String categorie) throws BusinessException;

    public List<articleBean> selectArticlesByCat(String categorie) throws BusinessException;
}
