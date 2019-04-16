package com.cd2cd.vo;

import java.util.List;

import com.cd2cd.domain.SysRole;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleVo extends SysRole {

	private static final long serialVersionUID = 1L;
	
	private List<String> authIds;

	public List<String> getAuthIds() {
		return authIds;
	}

	public void setAuthIds(List<String> authIds) {
		this.authIds = authIds;
	}

}
