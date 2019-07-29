package com.cd2cd.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("dataSource")
public class MultiDataSource extends MultiDataSourceAdapter {

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

		String tenantId = TenantThreadLocal.getTenantId();
		
		log.info("tenantId={}", tenantId);
		
		DataSource dataSource = dsMap.get(tenantId);
		if (dataSource != null) {
			return dataSource.getConnection();
		} else {
			try {
				dataSource = (DataSource) Class.forName(dataSourceType).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			try {
				String _url = url.replaceAll("\\{databaseName\\}", tenantId);
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
			return dataSource.getConnection();
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

}
