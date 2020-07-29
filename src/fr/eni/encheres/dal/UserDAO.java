package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;

public interface UserDAO {

    public userBean insertUser(userBean user) throws BusinessException;

    public userBean checkID(userBean log) throws BusinessException;
}
