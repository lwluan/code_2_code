package com.cd2cd.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd2cd.domain.SysAuthority;
import com.cd2cd.service.SysAuthorityService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Resource
	private SysAuthorityService sysAuthorityService;
	
	@RequestMapping(value = "/auths", produces="text/css;charset=UTF-8")
	@ResponseBody
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

}