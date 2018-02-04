package com.cd2cd.vo;

import java.util.List;

import com.cd2cd.domain.ProProject;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProProjectVo extends ProProject {

	private static final long serialVersionUID = 1L;

	private List<String> dbIds;

	public List<String> getDbIds() {
		return dbIds;
	}

	public void setDbIds(List<String> dbIds) {
		this.dbIds = dbIds;
	}

}
