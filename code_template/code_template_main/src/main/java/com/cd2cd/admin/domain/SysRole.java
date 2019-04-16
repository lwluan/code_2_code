package com.cd2cd.admin.domain;

import com.cd2cd.admin.domain.gen.SuperSysRole;
import java.io.Serializable;

public class SysRole extends SuperSysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer hasRole;

	public Integer getHasRole() {
		return hasRole;
	}

	public void setHasRole(Integer hasRole) {
		this.hasRole = hasRole;
	}

}