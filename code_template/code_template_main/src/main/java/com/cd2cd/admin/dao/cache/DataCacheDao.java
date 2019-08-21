package com.cd2cd.admin.dao.cache;

import com.cd2cd.admin.security.LoginUser;

public interface DataCacheDao {

	String getCacheData(String key);

	void setCacheData(String key, String value);

	String getCacheValidCode(String key);

	void setCacheValidCode(String key, String value);
	
	LoginUser getAdminLoginCacheData(String key);

	void setLoginCacheData(String key, LoginUser value);
}
