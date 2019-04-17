package com.cd2cd.util.mbg.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.h2.jdbcx.JdbcConnectionPool;

public class ConnectionPool {
    private static Map<String, ConnectionPool> cpMap = new HashMap<String, ConnectionPool>();
    private JdbcConnectionPool jdbcCP = null;

    private ConnectionPool(String path, String dbName, String user, String password) {
        String dbPath =path + 
        		(path.endsWith("/") || dbName.startsWith("/") ? "" : "/") + 
        		dbName;
        jdbcCP = JdbcConnectionPool.create("jdbc:h2:" + dbPath, user, password);
        jdbcCP.setMaxConnections(50);
    }

    public static ConnectionPool getInstance(String path, String dbName, String user, String password) {
    	String key = path+"_" + dbName;
        if (cpMap.get(key) == null) {
        	cpMap.put(key, new ConnectionPool(path, dbName, user, password));
        }
        return cpMap.get(key);
    }

    public Connection getConnection() throws SQLException {
        return jdbcCP.getConnection();
    }
}