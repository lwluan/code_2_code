package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProFunArg;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProFunArg extends SuperProFunArg implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 结点类型 */
	private String nodeType;
	
	/** VO成员变量做为参数时使用 */
	private Long fieldId;
	
	public ProFunArg() {
		super();
	}
	
	public ProFunArg(String nodeType) {
		super();
		this.nodeType = nodeType;
	}

	private List<ProFunArg> children;

	public List<ProFunArg> getChildren() {
		if(null == children || children.size() == 0) {
			return null;
		}
		return children;
	}

	public void setChildren(List<ProFunArg> children) {
		this.children = children;
	}
	
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	@Override
	public String toString() {
		return "ProFunArg [children=" + children + ", getId()=" + getId() + ", getFunId()=" + getFunId() + ", getPid()=" + getPid()
				+ ", getName()=" + getName() + ", getArgType()=" + getArgType() + ", getCollectionType()=" + getCollectionType()
				+ ", getValid()=" + getValid() + ", getComment()=" + getComment() + ", getCreateTime()=" + getCreateTime()
				+ ", getUpdateTime()=" + getUpdateTime() + ", toString()=" + super.toString() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + "]";
	}

}