package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.biddingBean;
import fr.eni.encheres.dal.BidDAO;
import fr.eni.encheres.dal.DAOFactory;

import java.util.List;

public class BidManager {
    private BidDAO bids;

    public BidManager() { this.bids = DAOFactory.getBidDAO(); }

    public List<biddingBean> displayOnGoingBids(int buyerId) throws BusinessException {
        return bids.selectMyOnGoingBids(buyerId);
    }
}
