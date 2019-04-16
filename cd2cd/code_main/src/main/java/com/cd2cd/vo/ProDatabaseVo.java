package com.cd2cd.vo;

import java.util.List;

import com.cd2cd.domain.ProDatabase;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProDatabaseVo extends ProDatabase {

	private static final long serialVersionUID = 1L;
	
	private List<ProTableVo> tables;

	public List<ProTableVo> getTables() {
		return tables;
	}

	public void setTables(List<ProTableVo> tables) {
		this.tables = tables;
	}
	
}
