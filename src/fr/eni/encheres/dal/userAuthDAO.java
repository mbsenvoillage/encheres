package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userAuth;

public interface userAuthDAO {

    public userAuth selectBySelector(String selector) throws BusinessException;

    public void insertNewToken(userAuth token) throws BusinessException;

    public void deleteAuthToken(int id) throws BusinessException;
}
