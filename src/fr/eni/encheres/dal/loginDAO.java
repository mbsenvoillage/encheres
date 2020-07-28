package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.loginBean;

public interface loginDAO {

    public loginBean checkID(loginBean log) throws BusinessException;
}
