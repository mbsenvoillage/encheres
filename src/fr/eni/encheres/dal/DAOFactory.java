package fr.eni.encheres.dal;

public abstract  class DAOFactory {

    public static UserDAO getUserDAO() {
        return new UserDAOJdbcImpl();
    }

    public static SaleDAO getSaleDAO() { return new SaleDAOJdbcImpl(); }

    public static BidDAO getBidDAO() { return new BidDAOJdbcImpl(); };
}
