package com.cd2cd.util.mbg.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class H2DatabaseUtil {
	static Map<String, ConnectionPool> h2DbMap= new HashMap<String, ConnectionPool>();
	private String _key;
	public H2DatabaseUtil(String path, String dbName, String user, String password) throws SQLException {
		_key = path+ "_" + dbName; 
		if(h2DbMap.containsKey(_key)) {
			return;
		} 
				
		ConnectionPool mConnectionPool = ConnectionPool.getInstance(path, dbName, user, password);
		h2DbMap.put(_key, mConnectionPool);
	}
	
	public Connection getConnection() throws SQLException {
		ConnectionPool mConnectionPool = h2DbMap.get(_key);
		Connection mConnection = mConnectionPool.getConnection();
		return mConnection;
	}
	
	
	public void exeSchema(String s) throws SQLException {
		Connection mConnection = getConnection();
		Statement mStatement = mConnection.createStatement();
		
		//创建USER_INFO表
		mStatement.execute(s);
		mStatement.close();
		mConnection.close();
	}
	
	public static void main(String[] args) throws SQLException {
		
		String path = "./test/";
		String dbName = "test_db";
		String user = "root"; 
		String password = "root";
		
		H2DatabaseUtil mH2DatabaseUtil = new H2DatabaseUtil(path, dbName, user, password);
		
	}

}
