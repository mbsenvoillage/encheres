package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;

public interface UserDAO {

    public userBean insertUser(userBean user) throws BusinessException;

    public userBean checkID(userBean log) throws BusinessException;

    public void checkPseudo(String pseudo) throws BusinessException;

    public void checkEmail(String email) throws BusinessException;
}
