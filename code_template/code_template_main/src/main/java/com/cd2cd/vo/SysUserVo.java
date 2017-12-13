package com.cd2cd.vo;

import com.cd2cd.domain.SysUser;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserVo extends SysUser {
	private static final long serialVersionUID = 1L;

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
