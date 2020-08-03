package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.biddingBean;

import java.util.List;

public interface BidDAO {

    public List<biddingBean> selectMyOnGoingBids(int buyerId) throws BusinessException;

    public List<biddingBean> selectAllOnGoingAucs() throws BusinessException;
}
