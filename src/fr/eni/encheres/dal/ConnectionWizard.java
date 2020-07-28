package fr.eni.encheres.dal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionWizard {
    private static DataSource dataSource;

    static {
        Context context;
        {
            try {
                context = new InitialContext();
                ConnectionWizard.dataSource = (DataSource) context.lookup("java:comp/env/jdbc/pool_cnx");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return ConnectionWizard.dataSource.getConnection();
    }
}
