package com.cd2cd.util;

import java.util.Iterator;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.comm.exceptions.ServiceBusinessException;
public class Validators {

	private static class ValidatorHolder {
		static Validator validator = new Validators().getValidator();
	}

	private final Logger logger = LoggerFactory.getLogger(Validators.class);
	private final ValidatorFactory factory;

	private Validators() {
		try {
			Configuration<?> factoryCfg = Validation.byDefaultProvider().configure();
			factory = factoryCfg.buildValidatorFactory();
		} catch (final ValidationException ex) {
			logger.error("Bean Validation provider can not be found, no validation will be performed");
			throw ex;
		}
	}

	private Validator getValidator() {
		return factory.getValidator();
	}

	/**
	 * 校验指定bean的单个属性值
	 * 
	 * @param bean 需要校验的bean
	 * @param name 需要校验的属性名
	 * @return 需要校验的属性的错误信息
	 */
	public static String validateProperty(Object bean, String name) {
		return parseConstraintViolations(ValidatorHolder.validator.validateProperty(bean, name));
	}

	/**
	 * 分组验证
	 * @param bean
	 * @param groups
	 */
	public static void validate(Object bean, Class<?>... groups) {
		String error = null;
		if(groups != null && groups.length > 0) {
			error = parseConstraintViolations(ValidatorHolder.validator.validate(bean, groups));
		} else {
			error = parseConstraintViolations(ValidatorHolder.validator.validate(bean));
		}
		if(StringUtils.isNotBlank(error)) {
			throw new ServiceBusinessException(ServiceCode.INVALID_PARAMS, error);
		}
	}
	
	/**
	 * 全部验证
	 * @param bean
	 */
	public static void validate(Object bean) {
		validate(bean, new Class<?>[]{});
	}

	public static String parseConstraintViolations(Set<ConstraintViolation<Object>> constraintViolations) {
		if (CollectionUtils.isEmpty(constraintViolations)) {
			return null;
		}

		Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();

		ConstraintViolation<Object> first = iterator.next();
		// 单个校验错误
		if (!iterator.hasNext()) {
			return first.getPropertyPath() + first.getMessage();
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first.getPropertyPath() + first.getMessage());
		}

		while (iterator.hasNext()) {
			buf.append(";");
			ConstraintViolation<?> elem = iterator.next();
			if (elem != null) {
				buf.append(elem.getPropertyPath() + elem.getMessage());
			}
		}

		return buf.toString();
	}

}