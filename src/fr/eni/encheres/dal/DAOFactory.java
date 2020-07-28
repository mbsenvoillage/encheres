package fr.eni.encheres.dal;

public abstract  class DAOFactory {

    public static loginDAO getLoginDAO() {
        return new ValidateLogin();
    }
}
