package com.cd2cd.admin.vo;

import com.cd2cd.admin.domain.SysRole;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleVo extends SysRole {
	private static final long serialVersionUID = 1L;

	private Integer current;
	private Integer pageSize;

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public static interface DeleteEntity {}
	public static interface AddEntityInfo {}
	public static interface ModifyEntityInfo {}

}
