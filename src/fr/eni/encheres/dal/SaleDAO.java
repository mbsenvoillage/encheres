package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Category;
import fr.eni.encheres.bo.articleBean;
import fr.eni.encheres.bo.userBean;

import java.util.List;

public interface SaleDAO {

    public articleBean inserArticle(articleBean article, userBean user) throws BusinessException;

    public int selectCatByName(String cat) throws BusinessException;

    public List<articleBean> selectAllArticles(String name, String cat, String status) throws BusinessException;

    public List<articleBean> selectOngoingAuctions(int userNb, String name, String cat, String status) throws BusinessException;

    public List<articleBean> selectUserWiningBids(Integer userNb, String name, String cat) throws BusinessException;

    public List<articleBean> selectUserItemsForSale(Integer userNb, String name, String cat, String status) throws BusinessException;


}
