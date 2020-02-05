package com.boyu.component;

import com.boyu.code.ServiceCode;
import com.boyu.vo.BaseRes;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * controller 统一验证 
 */
@Aspect
@Component
public class ControllerValidatorAspect {
	
	@Around("execution(* com.boyu..*.*(..)) && args(..,bindingResult)")
	public Object doAround(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
		if (bindingResult.hasErrors()) {
			BaseRes<String> res = new BaseRes<>();
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} else {
			return pjp.proceed();
		}
	}
}