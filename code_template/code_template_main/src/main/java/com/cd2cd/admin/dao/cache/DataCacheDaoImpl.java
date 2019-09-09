package com.cd2cd.admin.dao.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.mapper.SysAuthorityMapper;
import com.cd2cd.admin.mapper.SysUserMapper;
import com.cd2cd.admin.security.LoginUser;
import com.cd2cd.admin.util.Constants;
import com.cd2cd.admin.util.Constants.LoginUserType;
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
			String type = userObj.getString("type");
			
			if(LoginUserType.admin.name().equals(type)) {
				loginUser = loadAdminUserData(token, userObj);
			}/** else if(LoginUserType.api.name().equals(type)) {
				
			} **/
			
			loginUser.setType(type);
		}
		return loginUser;
	}

	private LoginUser loadAdminUserData(String token, JSONObject userObj) {
		int userId = userObj.getInteger("id");
		String username = userObj.getString("username");
		
		// 查询用户所有权限
		Set<String> roles = new HashSet<>();
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		if(Constants.ADMIN_NAME.equalsIgnoreCase(username)) {
			List<SysAuthority> list = sysAuthorityMapper.selectByExample(null);
			for(SysAuthority auth :list) {
				roles.add(auth.getGuid());
			}
		} else {
			List<SysAuthority> list = sysAuthorityMapper.userAuthority(userId);
			roles = getParentAndSelfAuth(list);
		}
		for(String role: roles) {
			roles.add(role);
			authorities.add(new SimpleGrantedAuthority(String.format("%s%s",Constants.ROLE_PREFIX,role)));
		}
		
		LoginUser loginUser = new LoginUser(username, "-", authorities);
		loginUser.setId(userId);
		loginUser.setAuthIds(roles);
					
		setLoginCacheData(token, loginUser);
		return loginUser;
	}
	
	private Set<String> getParentAndSelfAuth(List<SysAuthority> self) {
		List<SysAuthority> all = sysAuthorityMapper.selectByExample(null);
		Set<String> returnSet = new HashSet<>();
		Map<String, SysAuthority> map = new HashMap<>();
		for(SysAuthority sa: all) {
			map.put(sa.getGuid(), sa);
		}
		
		for(SysAuthority auth :self) {
			String guid = auth.getGuid();
			returnSet.add(guid);
			
			SysAuthority sa = map.get(guid);
			while(StringUtils.isNotBlank(sa.getPid())) {
				sa = map.get(sa.getPid());
				returnSet.add(sa.getGuid());
			}
		}
		
		return returnSet;
	}
	
	@Override
	public LoginUser getApiLoginCacheData(String token) {
		LoginUser loginUser = LOGIN_CACHE.getIfPresent(token);
		if(Objects.isNull(loginUser)) {
			/** api - 实现
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
			**/
		}
		return loginUser;
	}
}
