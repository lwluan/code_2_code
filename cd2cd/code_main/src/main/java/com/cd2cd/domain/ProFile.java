package com.cd2cd.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cd2cd.domain.gen.SuperProFile;

@SuppressWarnings("serial")
public class ProFile extends SuperProFile implements Serializable {

	private List<ProTableColumn> columns; // vo 列表使用
	private ProModule module;
	private List<ProFun> funs; // controller 使用 method方法列表
	private List<ProField> fields; // vo 使用,用于vo类成员域
	private List<ProField> validateMethods; // 需要验证生成的方法
	
	private Set<String> importTypes = new HashSet<>(); // 方法返回类型import
	private String superName; // 继承的父类
	
	private Map<String, Map<String, Set<String>>> propertyValid; // 成员变量验证
	
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

	public String getSuperName() {
		return superName;
	}

	public void setSuperName(String superName) {
		this.superName = superName;
	}

	public Map<String, Map<String, Set<String>>> getPropertyValid() {
		return propertyValid;
	}

	public void setPropertyValid(Map<String, Map<String, Set<String>>> propertyValid) {
		this.propertyValid = propertyValid;
	}

	public List<ProField> getValidateMethods() {
		return validateMethods;
	}

	public void setValidateMethods(List<ProField> validateMethods) {
		this.validateMethods = validateMethods;
	}
	
}