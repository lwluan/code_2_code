package com.cd2cd.vo;

import java.util.List;

import com.cd2cd.domain.ProTable;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProTableVo extends ProTable {

	private static final long serialVersionUID = 1L;

	private List<ProTableColumnVo> columns;

	public List<ProTableColumnVo> getColumns() {
		return columns;
	}

	public void setColumns(List<ProTableColumnVo> columns) {
		this.columns = columns;
	}
	
}
