package com.cd2cd.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cd2cd.domain.gen.SuperProFile;

@SuppressWarnings("serial")
public class ProFile extends SuperProFile implements Serializable {

	private List<ProTableColumn> columns; // vo 列表使用
	private ProModule module;
	private List<ProFun> funs; // controller 使用 method方法列表
	private List<ProField> fields; // vo 使用,用于vo类成员域
	
	private Set<String> importTypes = new HashSet<String>(); // 方法返回类型import
	
	public List<ProTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ProTableColumn> columns) {
		this.columns = columns;
	}

	public ProModule getModule() {
		return module;
	}

	public void setModule(ProModule module) {
		this.module = module;
	}

	public List<ProFun> getFuns() {
		return funs;
	}

	public void setFuns(List<ProFun> funs) {
		this.funs = funs;
	}

	public Set<String> getImportTypes() {
		return importTypes;
	}

	public void setImportTypes(Set<String> importTypes) {
		this.importTypes = importTypes;
	}

	public List<ProField> getFields() {
		return fields;
	}

	public void setFields(List<ProField> fields) {
		this.fields = fields;
	}
	
}