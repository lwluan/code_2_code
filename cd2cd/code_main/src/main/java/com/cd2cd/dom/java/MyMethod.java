package com.cd2cd.dom.java;

import org.mybatis.generator.api.dom.java.Method;

public class MyMethod extends Method {

	private String customComment;
	private String genStr;
	
	public MyMethod(String name) {
		super(name);
	}
	
	public String getCustomComment() {
		return customComment;
	}
	public void setCustomComment(String customComment) {
		this.customComment = customComment;
	}
	public String getGenStr() {
		return genStr;
	}
	public void setGenStr(String genStr) {
		this.genStr = genStr;
	}
	
	
}
