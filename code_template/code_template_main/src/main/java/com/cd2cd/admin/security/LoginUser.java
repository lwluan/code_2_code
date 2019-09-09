package com.cd2cd.admin.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 登录用户信息
 */
public class LoginUser extends User {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String type; // admin/custome
	private Set<String> authIds;

	public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<String> getAuthIds() {
		return authIds;
	}

	public void setAuthIds(Set<String> authIds) {
		this.authIds = authIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
