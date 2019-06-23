package com.cd2cd.admin.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.security.ApplicationSecurity;
import com.cd2cd.admin.security.LoginUser;
import com.cd2cd.admin.security.Md5PasswordEncoder;
import com.cd2cd.admin.security.MyUserDetailsService;
import com.cd2cd.admin.service.SysAuthorityService;
import com.cd2cd.admin.util.JWTHelperUtil;
import com.cd2cd.admin.vo.BaseRes;

@RestController
public class CommController {

	@Resource
	private SysAuthorityService sysAuthorityService;
	
	@Resource
	private MyUserDetailsService myUserDetailsService;
	
	@Resource
	private JWTHelperUtil jWTHelperUtil;
	
	@RequestMapping(value = "/auths", produces="text/css;charset=UTF-8")
	protected String template(Map<String, Object> model) {
		
		// 当前登录用户所有权限
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		// 系统当中所有权限
		List<SysAuthority> allAuthorities = sysAuthorityService.getAllAuthority();
		
		// 返回所有登录用户未获得的所有权限
		allAuthorities.removeAll(authorities);
		
		Collection<GrantedAuthority> noHasAuths = getNoHasAuthorities(authorities, allAuthorities);
		
		StringBuffer auths = new StringBuffer();
		for( GrantedAuthority  ga: noHasAuths ) {
			auths.append("." + ga.getAuthority() + "{ display: none !important;} \n");
		}
		return auths.toString();
	}
	
	private List<GrantedAuthority> getNoHasAuthorities(Collection<GrantedAuthority> authorities, List<SysAuthority> allAuthorities) {
		List<GrantedAuthority> all = new ArrayList<GrantedAuthority>();
		for(SysAuthority auth: allAuthorities) {
			String guid = auth.getGuid();
			all.add(new SimpleGrantedAuthority("ROLE_" + guid));
		}
		all.removeAll(authorities);
		return all;
	}

	@RequestMapping(value = ApplicationSecurity.LOGIN_PATH, method = RequestMethod.POST)
	public BaseRes<String> userLogin(String username, String password) {
		BaseRes<String> res = new BaseRes<String>();
		
		LoginUser loginUser = myUserDetailsService.loadUserByUsername(username);
		password = Md5PasswordEncoder.md5Encode(password);
		if( loginUser.getPassword().equals(password) ) {
			String token = jWTHelperUtil.getToken(loginUser);
			res.setData(token);
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.LOGIN_ERROR);
		}
		
		return res;
	}
}