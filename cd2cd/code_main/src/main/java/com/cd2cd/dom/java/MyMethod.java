package com.cd2cd.dom.java;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.Method;

public class MyMethod extends Method {

	private List<String> customComment = new ArrayList<>();
	private String genStr;

	public MyMethod(String name) {
		super(name);
	}

	public List<String> getCustomComment() {
		return customComment;
	}

	public void setCustomComment(List<String> customComment) {
		this.customComment = customComment;
	}

	public String getGenStr() {
		return genStr;
	}

	public void setGenStr(String genStr) {
		this.genStr = genStr;
	}

}
