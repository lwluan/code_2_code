package com.cd2cd.domain;

import java.io.Serializable;
import java.util.List;

import com.cd2cd.domain.gen.SuperProFun;

public class ProFun extends SuperProFun implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<ProFunArg> args; // 生成使用
    
	public List<ProFunArg> getArgs() {
		return args;
	}

	public void setArgs(List<ProFunArg> args) {
		this.args = args;
	}
}