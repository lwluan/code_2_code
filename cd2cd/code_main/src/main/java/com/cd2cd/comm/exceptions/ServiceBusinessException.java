package com.cd2cd.comm.exceptions;

import org.springframework.http.HttpStatus;

import com.cd2cd.comm.ServiceCode;

/**
 * 200
 */
public class ServiceBusinessException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceBusinessException(ServiceCode serviceCode, String message) {
		this.setServiceCode(serviceCode);
		this.setMessage(message);
		this.setHttpStatus(HttpStatus.OK);
	}

	public ServiceBusinessException(ServiceCode serviceCode) {
		this.setServiceCode(serviceCode);
		this.setMessage(serviceCode.msg);
		this.setHttpStatus(HttpStatus.OK);
	}

}
