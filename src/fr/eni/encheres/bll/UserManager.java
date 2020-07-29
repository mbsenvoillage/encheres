package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.UserDAO;

public class UserManager {
    private UserDAO user;

    public UserManager() {
        this.user = DAOFactory.getUserDAO();
    }

    public userBean checkCredentials(userBean login) throws BusinessException {
        return this.user.checkID(login);
    }
}
