package com.cd2cd.admin.comm.exceptions;

import org.springframework.http.HttpStatus;

import com.cd2cd.admin.comm.ServiceCode;

/**
 * bad request
 * 400
 */
public class BadRequestException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(ServiceCode serviceCode, String message) {
		this.setServiceCode(serviceCode);
		this.setMessage(message);
		this.setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public BadRequestException(ServiceCode serviceCode) {
		this.setServiceCode(serviceCode);
		this.setMessage(serviceCode.msg);
		this.setHttpStatus(HttpStatus.BAD_REQUEST);
	}

}
