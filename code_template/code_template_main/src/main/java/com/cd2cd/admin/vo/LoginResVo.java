package com.cd2cd.admin.vo;

import java.util.Set;

public class LoginResVo {
	
	private String token;
	private String type;
	private Set<String> authIds;

	public LoginResVo() {}
	public LoginResVo(String token, Set<String> authIds) {
		super();
		this.token = token;
		this.authIds = authIds;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
