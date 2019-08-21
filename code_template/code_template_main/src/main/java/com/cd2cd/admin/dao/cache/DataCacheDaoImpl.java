package com.cd2cd.admin.dao.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.mapper.SysAuthorityMapper;
import com.cd2cd.admin.mapper.SysUserMapper;
import com.cd2cd.admin.security.LoginUser;
import com.cd2cd.admin.util.JWTHelperUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class DataCacheDaoImpl implements DataCacheDao {

	// 短信验证码缓存
	private static final Cache<String, String> SMS_CACHE = CacheBuilder.newBuilder().maximumSize(100000)
			.expireAfterWrite(5, TimeUnit.MINUTES) // 5分钟
			.build();

	// 用户登录
	private static final Cache<String, LoginUser> LOGIN_CACHE = CacheBuilder.newBuilder().maximumSize(100000)
			.expireAfterAccess(30, TimeUnit.MINUTES).build();
	
	@Resource
	private JWTHelperUtil jWTHelperUtil;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;

	@Override
	public String getCacheValidCode(String key) {
		return SMS_CACHE.getIfPresent(key);
	}

	@Override
	public void setCacheValidCode(String key, String value) {
		SMS_CACHE.put(key, value);
	}


	@Override
	public void setLoginCacheData(String key, LoginUser value) {
		LOGIN_CACHE.put(key, value);
	}

	@Override
	public String getCacheData(String key) {
		return SMS_CACHE.getIfPresent(key);
	}

	@Override
	public void setCacheData(String key, String value) {
		SMS_CACHE.put(key, value);
	}

	@Override
	public LoginUser getAdminLoginCacheData(String token) {
		LoginUser loginUser = LOGIN_CACHE.getIfPresent(token);
		if(Objects.isNull(loginUser)) {
			
			String userStr = jWTHelperUtil.getClaimByToken(token);
			JSONObject userObj = JSONObject.parseObject(userStr);
			int userId = userObj.getInteger("id");
			String username = userObj.getString("username");
			
			// 查询用户所有权限
			Set<String> roles = new HashSet<>();
			Set<GrantedAuthority> authorities = new HashSet<>();
			List<SysAuthority> list = sysAuthorityMapper.userAuthority(userId);
			for(SysAuthority auth: list) {
				String role = auth.getGuid();
				roles.add(role);
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			loginUser = new LoginUser(username, "-", authorities);
			loginUser.setAuthIds(new ArrayList<>(roles));
			loginUser.setId(userId);
			setLoginCacheData(token, loginUser);
		}
		return loginUser;
	}

}
