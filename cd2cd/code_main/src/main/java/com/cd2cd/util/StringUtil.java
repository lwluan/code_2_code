package com.cd2cd.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {

	/** 首字母大小 */
	public static String firstUpCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1, value.length()); 
	}
	
}
