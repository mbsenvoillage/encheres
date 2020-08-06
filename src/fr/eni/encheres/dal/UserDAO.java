package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;

public interface UserDAO {

    public userBean insertUser(userBean user) throws BusinessException;

    public userBean checkID(userBean log) throws BusinessException;

    public void checkPseudo(String pseudo) throws BusinessException;

    public void checkEmail(String email) throws BusinessException;

    public userBean selectUserPrivateInfo(String pseudo) throws BusinessException;

    public userBean updateUserInfo (userBean user) throws BusinessException;

    public boolean passwordIsValid(userBean login) throws BusinessException;

    public void deleteUser(int nbutilisateur) throws BusinessException;

    public void updateUserCredit(int credit, int userNb) throws BusinessException;
}
