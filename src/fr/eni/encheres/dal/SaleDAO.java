package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.*;

import java.util.List;

public interface SaleDAO {

    public articleBean inserArticle(articleBean article, userBean user) throws BusinessException;

    public int selectCatByName(String cat) throws BusinessException;

    public List<articleBean> selectAllArticles(String name, String cat, String status) throws BusinessException;

    public List<articleBean> selectOngoingAuctions(int userNb, String name, String cat, String status) throws BusinessException;

    public List<articleBean> selectUserWinningBids(Integer userNb, String name, String cat) throws BusinessException;

    public List<articleBean> selectUserItemsForSale(Integer userNb, String name, String cat, String status) throws BusinessException;

    public biddingBean insertBid(biddingBean bid) throws BusinessException;

    public void updateArtSalePrice(int price, int artNb) throws BusinessException;

    public int checkUserCredit(int userNb) throws BusinessException;

    public articleBean detailAuction(int artNb) throws BusinessException;

    public biddingBean selectHighestBidder(int artNb) throws BusinessException;

    public void insertRetrait(PickUp retrait, int artNb) throws BusinessException;

    public void updateSaleStatus() throws BusinessException;

    public articleBean updateArticle(articleBean article, userBean user) throws BusinessException;

    public void updateRetrait(PickUp retrait, int artNb) throws BusinessException;

    public void deleteArticle(int artNb) throws BusinessException;
}
