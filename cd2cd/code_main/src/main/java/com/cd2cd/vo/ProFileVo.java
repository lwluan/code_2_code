package com.cd2cd.vo;

import com.cd2cd.domain.ProFile;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProFileVo extends ProFile {

	private static final long serialVersionUID = 5868673662151017888L;
	private ProTableVo table;

	public ProTableVo getTable() {
		return table;
	}

	public void setTable(ProTableVo table) {
		this.table = table;
	}

}
