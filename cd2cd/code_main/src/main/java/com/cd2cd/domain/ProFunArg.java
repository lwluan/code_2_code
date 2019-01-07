package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProFunArg;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProFunArg extends SuperProFunArg implements Serializable {
	private static final long serialVersionUID = 1L;

	
	public ProFunArg() {
		super();
	}

	private List<ProFunArg> childArgs;

	public List<ProFunArg> getChildArgs() {
		return childArgs;
	}

	public void setChildArgs(List<ProFunArg> childArgs) {
		this.childArgs = childArgs;
	}

	@Override
	public String toString() {
		return "ProFunArg [childArgs=" + childArgs + ", getId()=" + getId() + ", getFunId()=" + getFunId() + ", getPid()=" + getPid()
				+ ", getName()=" + getName() + ", getArgType()=" + getArgType() + ", getCollectionType()=" + getCollectionType()
				+ ", getValid()=" + getValid() + ", getComment()=" + getComment() + ", getCreateTime()=" + getCreateTime()
				+ ", getUpdateTime()=" + getUpdateTime() + ", toString()=" + super.toString() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + "]";
	}

	
}