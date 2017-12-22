package com.cd2cd.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjDataWrapper<T, E, H> {

	private T data1;
	private E data2;
	private H data3;

	public T getData1() {
		return data1;
	}

	public void setData1(T data1) {
		this.data1 = data1;
	}

	public E getData2() {
		return data2;
	}

	public void setData2(E data2) {
		this.data2 = data2;
	}

	public H getData3() {
		return data3;
	}

	public void setData3(H data3) {
		this.data3 = data3;
	}

}
