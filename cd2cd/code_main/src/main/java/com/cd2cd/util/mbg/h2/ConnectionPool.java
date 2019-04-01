package com.cd2cd.util.mbg.h2;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public class ConnectionPool {
    private static ConnectionPool cp = null;
    private JdbcConnectionPool jdbcCP = null;

    private ConnectionPool(String path, String dbName, String user, String password) {
        String dbPath =path + 
        		(path.endsWith("/") || dbName.startsWith("/") ? "" : "/") + 
        		dbName;
        jdbcCP = JdbcConnectionPool.create("jdbc:h2:" + dbPath, user, password);
        jdbcCP.setMaxConnections(50);
    }

    public static ConnectionPool getInstance(String path, String dbName, String user, String password) {
        if (cp == null) {
            cp = new ConnectionPool(path, dbName, user, password);
        }
        return cp;
    }

    public Connection getConnection() throws SQLException {
        return jdbcCP.getConnection();
    }
}