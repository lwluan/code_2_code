package com.cd2cd.vo;

import java.util.List;

import com.cd2cd.domain.SysUser;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserVo extends SysUser {
	private static final long serialVersionUID = 1L;

	private List<Integer> roles;
	
	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "SysUserVo [getId()=" + getId() + ", getUsername()="
				+ getUsername() + ", getPasswd()=" + getPassword()
				+ ", getCreateTime()=" + getCreateTime() + ", getUpdateTime()="
				+ getUpdateTime() + ", toString()=" + super.toString()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ "]";
	}
}
