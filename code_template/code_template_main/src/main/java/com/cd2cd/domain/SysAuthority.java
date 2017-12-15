package com.cd2cd.domain;

import com.cd2cd.domain.gen.SuperSysAuthority;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthority extends SuperSysAuthority implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer checked;

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

}