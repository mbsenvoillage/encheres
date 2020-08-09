package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userAuth;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.userAuthDAO;

public class userAuthManager {

    private userAuthDAO auth;

    public userAuthManager() {
        this.auth = DAOFactory.getUserAuthDAO();
    }

    public userAuth findBySelector(String selector) throws BusinessException {
        return auth.selectBySelector(selector);
    }

    public void createNewAuthToken(userAuth token) throws BusinessException {
        auth.insertNewToken(token);
    }

    public void deleteToken(int id) throws BusinessException {
        auth.deleteAuthToken(id);
    }
}
