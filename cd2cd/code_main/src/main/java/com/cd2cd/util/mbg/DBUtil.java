package com.cd2cd.util.mbg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Map<String, Object>> exeQuerySql(String sql, String host, int port, String user, String password, String db) {
		
		String url = "jdbc:mysql://"+host+":"+port+"/" + db;
		
		List<Map<String, Object>> tabData = new ArrayList<Map<String, Object>>();
		Connection mConnection = null;
		Statement mStatement = null;
		ResultSet mResultSet = null;
		
		
		try {
			mConnection = DriverManager.getConnection(url, user, password);
			
			mStatement = mConnection.createStatement();
			mResultSet = mStatement.executeQuery(sql);
			
			
			ResultSetMetaData rsmd = mResultSet.getMetaData();
			int count = rsmd.getColumnCount();

			String[] columns = new String[count];
			for( int i=0; i<count; i++ ) {
				String cName = rsmd.getColumnLabel(i+1);
				
				columns[i] = cName;
			}
			
			
			while( mResultSet.next() ) {
				
				Map<String, Object> row = new HashMap<String, Object>();
				for( String c: columns ) {
					row.put(c, mResultSet.getObject(c));
				}
				tabData.add(row);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally  {
			
			if( mConnection != null ) {
				try {
					mConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if( mStatement != null ) {
				try {
					mStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if( mResultSet != null ) {
				try {
					mResultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return tabData;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// List<Map<String, Object>> list = 
//		exeQuerySql("show table status;", "localhost", 3306, "root", "root", "test");
		
		
		String sql = "create table test01";
		
		exeQuerySql(sql, "localhost", 3306, "root", "root", "test");
		
		
	}

}
