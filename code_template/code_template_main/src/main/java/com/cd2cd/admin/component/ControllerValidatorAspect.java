package com.cd2cd.admin.component;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.vo.BaseRes;

/**
 * controller 统一验证 
 */
@Aspect
@Component
public class ControllerValidatorAspect {
	
	@Around("execution(* com.cd2cd.controller..*.*(..)) && args(..,bindingResult)")
	public Object doAround(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
		if (bindingResult.hasErrors()) {
			BaseRes<String> res = new BaseRes<String>();
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} else {
			return pjp.proceed();
		}
	}
}