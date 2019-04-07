package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProFile;

@SuppressWarnings("serial")
public class ProFile extends SuperProFile implements Serializable {

	private List<ProTableColumn> columns; // vo 列表使用
	private ProModule module;
	private List<ProFun> funs; // controller 使用 method方法列表
	
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

}