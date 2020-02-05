package com.cd2cd.admin.comm.exceptions;

import org.springframework.http.HttpStatus;

import com.cd2cd.admin.comm.ServiceCode;

/**
 * 404
 */
public class ResourceNotFoundException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(ServiceCode serviceCode, String message) {
		this.setServiceCode(serviceCode);
		this.setMessage(message);
		this.setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public ResourceNotFoundException(ServiceCode serviceCode) {
		this.setServiceCode(serviceCode);
		this.setMessage(serviceCode.msg);
		this.setHttpStatus(HttpStatus.NOT_FOUND);
	}
}
