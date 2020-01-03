package com.boyu.vo;

import com.boyu.code.ServiceCode;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRes<T> {

	private T data;
	private String msg;
	private Integer code;
	private ServiceCode serviceCode;
	
	public BaseRes() {}

	public BaseRes(T data, ServiceCode serviceCode) {
		this.data = data;
		this.serviceCode = serviceCode;
	}

	public BaseRes(ServiceCode serviceCode) {
		this.serviceCode = serviceCode;
	}
	public BaseRes(ServiceCode serviceCode, T data) {
		this.serviceCode = serviceCode;
		this.data = data;
	}
	
	public void setServiceCode(ServiceCode serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getMsg() {
		String _msg = null;
		if( null != serviceCode ) {
			if( StringUtils.isNotEmpty(msg) ) {
				_msg = msg + "|" + serviceCode.msg;
			} else {
				_msg = serviceCode.msg;
			}
		} else {
			_msg = msg;
		}
		return _msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		if( null != serviceCode ) {
			return serviceCode.code;
		}
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
