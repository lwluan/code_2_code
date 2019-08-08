package com.cd2cd.admin.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 对像转换
 */
public class BeanUtil {

	private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	public static <E> List<E> voConvertList(List<?> list, Class<E> target) {
		List<E> retList = new ArrayList<>();

		for (Object o : list) {
			E targetObj = voConvert(o, target); 
			retList.add(targetObj);
		}
		return retList;
	}
	
	public static <E> List<E> voConvertListIgnore(List<?> list, Class<E> target, String ...ignoreProperties) {
		List<E> retList = new ArrayList<>();

		for (Object o : list) {
			E targetObj = voConvertIgnore(o, target, ignoreProperties); 
			retList.add(targetObj);
		}
		return retList;
	}

	public static <E> E voConvert(Object o, Class<E> target) {
		E targetObj = null;
		try {
			targetObj = target.newInstance();
			BeanUtils.copyProperties(o, targetObj);
		} catch (Exception e) {
			logger.error("e={}", e.getMessage(), e);
		}
		return targetObj;
	}
	
	public static <E> E voConvertIgnore(Object o, Class<E> target, String ...ignoreProperties) {
		E targetObj = null;
		try {
			targetObj = target.newInstance();
			BeanUtils.copyProperties(o, targetObj, ignoreProperties);
		} catch (Exception e) {
			logger.error("e={}", e.getMessage(), e);
		}
		return targetObj;
	}
	
	public static <E> List<E> voConvertListHasIn(List<?> list, Class<E> target, String ...copyProperties) {
		List<E> retList = new ArrayList<>();

		for (Object o : list) {
			E targetObj = voConvertHasIn(o, target, copyProperties); 
			retList.add(targetObj);
		}
		return retList;
	}
	
	public static <E> E voConvertHasIn(Object o, Class<E> target, String ...copyProperties) {
		E targetObj = null;
		try {
			targetObj = target.newInstance();
			copyProperties(o, targetObj, copyProperties);
		} catch (Exception e) {
			logger.error("e={}", e.getMessage(), e);
		}
		return targetObj;
	}
	
	private static void copyProperties(Object source, Object target, String... copyProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Assert.notNull(copyProperties, "copyProperties must not be null");

		Class<?> actualEditable = target.getClass();
		
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> hasProList = Arrays.asList(copyProperties);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (hasProList == null || hasProList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null &&
							ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
						catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}
}