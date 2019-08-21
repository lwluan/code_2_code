package com.cd2cd.admin.vo;

import java.util.List;

public class LoginResVo {
	
	private String token;
	private List<String> authIds;

	public LoginResVo() {}
	public LoginResVo(String token, List<String> authIds) {
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

	public List<String> getAuthIds() {
		return authIds;
	}

	public void setAuthIds(List<String> authIds) {
		this.authIds = authIds;
	}

}
