package com.cd2cd.domain;

import com.cd2cd.domain.gen.SuperProTable;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProTable extends SuperProTable implements Serializable {

	private String identityPrimaryKey;

	public String getIdentityPrimaryKey() {
		return identityPrimaryKey;
	}

	public void setIdentityPrimaryKey(String identityPrimaryKey) {
		this.identityPrimaryKey = identityPrimaryKey;
	}
	
	public String getJavaTableName() {
		
		String name = getName();
		String newName = "";
		String[] ss = name.split("_");
		for(String s: ss) {
			s = s.substring(0, 1).toUpperCase() + s.substring(1);
			newName = newName + s;
		}
		
		return newName;
	}
}