package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProFunArg;

public class ProFunArg extends SuperProFunArg implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ProFunArg> childArgs;

	public List<ProFunArg> getChildArgs() {
		return childArgs;
	}

	public void setChildArgs(List<ProFunArg> childArgs) {
		this.childArgs = childArgs;
	}

}