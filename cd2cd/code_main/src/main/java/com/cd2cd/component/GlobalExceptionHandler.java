package com.cd2cd.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.comm.exceptions.BaseRuntimeException;
import com.cd2cd.vo.BaseRes;

/**
 * 全局异常捕获
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 业务统一异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler({BaseRuntimeException.class})
    @ResponseBody
    ResponseEntity<Object> baseRuntimeException(BaseRuntimeException e){
		
		LOG.error("code={}, msg={}, error={}", e.getServiceCode().code, e.getServiceCode().msg, e.getMessage(), e);
		
		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(e.getServiceCode());
        return new ResponseEntity<Object>(res, e.getHttpStatus());
    }
	
	
	/**
	 * 全局未知错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Exception.class})
    @ResponseBody
    BaseRes<String> allException(Exception e){
		
		LOG.error("errorMsg={}", e.getMessage(), e);
		
		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(ServiceCode.FAILED);
        return res;
    }
	
	/**
	 * error
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Error.class})
    @ResponseBody
    BaseRes<String> allError(Error e){
		
		LOG.error("errorMsg={}", e.getMessage(), e);
		
		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(ServiceCode.FAILED);
        return res;
    }
	
}
