package org.hibernate.engine.jdbc;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;

public class SQLXMLCreator {
    public static final SQLXMLCreator INSTANCE = new SQLXMLCreator();

    public SQLXML createSQLXML(SessionFactory sessionFactory) {
        Connection connection = null;
        try {
            connection = sessionFactory
                    .getSessionFactoryOptions()
                    .getServiceRegistry()
                    .getService(ConnectionProvider.class)
                    .getConnection();

            return connection.createSQLXML();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
