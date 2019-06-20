package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProTable;
import com.cd2cd.util.StringUtil;

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
		return StringUtil.getJavaTableName(getName());
	}
}