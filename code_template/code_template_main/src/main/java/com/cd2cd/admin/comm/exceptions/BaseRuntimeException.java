package com.cd2cd.admin.comm.exceptions;

import org.springframework.http.HttpStatus;

import com.cd2cd.admin.comm.ServiceCode;

/**
 * 其他异常都基于此类扩展
 */
public abstract class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected HttpStatus httpStatus;
	private ServiceCode serviceCode;
	private String message;

	public BaseRuntimeException() {
		this.serviceCode = ServiceCode.SUCCESS;
		this.message = ServiceCode.SUCCESS.msg;
	}

	public BaseRuntimeException(ServiceCode serviceCode, String message) {
		this.serviceCode = serviceCode;
		this.message = message;
	}

	public ServiceCode getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(ServiceCode serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
