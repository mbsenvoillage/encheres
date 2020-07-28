package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.loginBean;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.loginDAO;

public class loginManager {
    private loginDAO login;

    public loginManager() {
        this.login = DAOFactory.getLoginDAO();
    }

    public loginBean checkCredentials(loginBean login) throws BusinessException {
        return this.login.checkID(login);
    }
}
