package com.cd2cd.admin.comm.exceptions;

import org.springframework.http.HttpStatus;

import com.cd2cd.admin.comm.ServiceCode;

/**
 * 500
 */
public class ServerInternalErrorException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public ServerInternalErrorException(ServiceCode serviceCode, String message) {
		this.setServiceCode(serviceCode);
		this.setMessage(message);
		this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ServerInternalErrorException(ServiceCode serviceCode) {
		this.setServiceCode(serviceCode);
		this.setMessage(serviceCode.msg);
		this.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
