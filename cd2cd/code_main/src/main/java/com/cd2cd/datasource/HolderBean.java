package com.cd2cd.datasource;


public class HolderBean {
	private String t; // 租户
	private String c; // 渠道
	private String a; // 应用ID

	public HolderBean() {
		super();
	}

	public HolderBean(String t, String c, String a) {
		super();
		this.t = t;
		this.c = c;
		this.a = a;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	@Override
	public String toString() {
		return "HolderBean [t=" + t + ", c=" + c + ", a=" + a + "]";
	}
}
