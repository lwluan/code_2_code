package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProTable;

@SuppressWarnings("serial")
public class ProTable extends SuperProTable implements Serializable {

	private String identityPrimaryKey;
	private List<ProTableColumn> columns;

	public String getIdentityPrimaryKey() {
		return identityPrimaryKey;
	}

	public void setIdentityPrimaryKey(String identityPrimaryKey) {
		this.identityPrimaryKey = identityPrimaryKey;
	}
	
	public List<ProTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ProTableColumn> columns) {
		this.columns = columns;
	}

	public String getJavaTableName() {

		String name = getName();
		String newName = "";
		String[] ss = name.split("_");
		for (String s : ss) {
			s = s.substring(0, 1).toUpperCase() + s.substring(1);
			newName = newName + s;
		}

		return newName;
	}
}