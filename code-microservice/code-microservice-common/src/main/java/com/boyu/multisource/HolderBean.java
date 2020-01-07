package com.boyu.multisource;


public class HolderBean {
	private String t; // 租户
	private String a; // 应用ID
	private String c; // 渠道ID-用于渠道登录时使用
	
	public HolderBean() {
		super();
	}

	public HolderBean(String t, String a) {
		super();
		this.t = t;
		this.a = a;
	}
	
	public HolderBean(String t, String a, String c) {
		super();
		this.t = t;
		this.a = a;
		this.c = c;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	
	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return "HolderBean [t=" + t + ", a=" + a + ", c=" + c + "]";
	}

}
