package com.cd2cd.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.SysAuthority;
import com.cd2cd.security.ApplicationSecurity;
import com.cd2cd.security.LoginUser;
import com.cd2cd.security.Md5PasswordEncoder;
import com.cd2cd.security.MyUserDetailsService;
import com.cd2cd.service.SysAuthorityService;
import com.cd2cd.util.JWTHelperUtil;
import com.cd2cd.vo.BaseRes;

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
		password = Md5PasswordEncoder.md5Encode(username+password);
		if( true) { //loginUser.getPassword().equals(password) ) 
		
			String token = jWTHelperUtil.getToken(loginUser);
			res.setData(token);
			res.setServiceCode(ServiceCode.SUCCESS);
		}
//		else {
//			res.setServiceCode(ServiceCode.LOGIN_ERROR);
//		}
		
		return res;
	}
}