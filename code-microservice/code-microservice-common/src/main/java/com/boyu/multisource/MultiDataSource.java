package com.boyu.multisource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiDataSource implements DataSource {
	private static final Logger log = LoggerFactory.getLogger(MultiDataSource.class);

	protected String dataSourceType;
	public String driverClassName;
	public Integer maxActive;
	public Integer maxIdle;
	public Integer minIdle;
	public Integer initialSize;
	public Integer maxWait;
	public String url;
	public String username;
	public String password;

	protected Map<String, DataSource> dsMap = new HashMap<>();

	public Connection getConnection() throws SQLException {
		DataSource dataSource = getDataSource();
		return dataSource.getConnection();
	}
	
	private DataSource getDataSource() {
		String tenantId = TenantThreadLocal.getTenantId();

		DataSource dataSource = dsMap.get(tenantId);
		if (dataSource != null) {
			return dataSource;
		} else {
			try {
				dataSource = (DataSource) Class.forName(dataSourceType).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			try {
				String _url = url.replaceAll("\\{"+TenantThreadLocal.defaulTenantId+"\\}", tenantId);
				BeanUtils.setProperty(dataSource, "driverClassName", driverClassName);
				BeanUtils.setProperty(dataSource, "maxActive", driverClassName);
				BeanUtils.setProperty(dataSource, "maxIdle", maxIdle);
				BeanUtils.setProperty(dataSource, "minIdle", minIdle);
				BeanUtils.setProperty(dataSource, "initialSize", initialSize);
				BeanUtils.setProperty(dataSource, "maxWait", maxWait);
				BeanUtils.setProperty(dataSource, "url", _url);
				BeanUtils.setProperty(dataSource, "username", username);
				BeanUtils.setProperty(dataSource, "password", password);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("设置数据源参数时异常");
			}

			dsMap.put(tenantId, dataSource);
			return dataSource;
		}
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		String tenantId = TenantThreadLocal.getTenantId();
		DataSource dataSource = dsMap.get(tenantId);
		return dataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		DataSource dataSource = getDataSource();
		dataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		DataSource dataSource = getDataSource();
		dataSource.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		DataSource dataSource = getDataSource();
		return dataSource.getLoginTimeout();
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		DataSource dataSource = getDataSource();
		return dataSource.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		DataSource dataSource = getDataSource();
		return dataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		DataSource dataSource = getDataSource();
		return dataSource.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		DataSource dataSource = getDataSource();
		return dataSource.getConnection(username, password);
	}

}
