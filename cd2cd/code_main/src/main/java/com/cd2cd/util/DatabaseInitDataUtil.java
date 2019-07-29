package com.cd2cd.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitDataUtil {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseInitDataUtil.class);
	
	@Value("${spring.datasource.driver-class-name:}")
	private String driverClassName;
	
	@Value("${spring.datasource.url:}")
	private String url;
	
	@Value("${spring.datasource.username:}")
	private String username;
	
	@Value("${spring.datasource.password:}")
	private String password;
	
	public void initDatabase() throws SQLException, FileNotFoundException, IOException {
		
		if("org.h2.Driver".equalsIgnoreCase(driverClassName)) {
			JdbcConnectionPool mJdbcConnectionPool = JdbcConnectionPool.create(url, username, password);
			Connection mConnection = mJdbcConnectionPool.getConnection();
			Statement mStatement = mConnection.createStatement();
			ResultSet rs = mStatement.executeQuery("select * from sys_user");
			
			if( ! rs.next()) {
				/** 数据库未初始化数据时，向表中插入初始化数据 */
				rs.close();
				
				InputStream inputStream = DatabaseInitDataUtil.class.getResourceAsStream("/sql/data.sql");
				if(inputStream != null) {
					String sqlContent = IOUtils.toString(inputStream, "utf-8");
					LOG.info("sqlContent={}", sqlContent);
					mStatement.execute(sqlContent);
				}
			}
		}	
	}
	public static void main(String[] args) {
		
		System.out.println(DatabaseInitDataUtil.class.getResource("/sql/data.sql").getPath());
	}

}
