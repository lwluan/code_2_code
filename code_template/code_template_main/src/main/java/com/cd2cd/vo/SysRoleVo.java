package com.cd2cd.vo;

import java.util.List;

import com.cd2cd.domain.SysRole;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleVo extends SysRole {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> authIds;

	public List<Integer> getAuthIds() {
		return authIds;
	}

	public void setAuthIds(List<Integer> authIds) {
		this.authIds = authIds;
	}

}
