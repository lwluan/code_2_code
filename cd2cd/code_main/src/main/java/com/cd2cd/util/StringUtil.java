package com.cd2cd.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {

	/** 首字母大小 */
	public static String firstUpCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1, value.length()); 
	}
	
	/**
	 * 数据库名转为java类名
	 * @param name
	 * @return
	 */
	public static String getJavaTableName(String name) {
		String newName = "";
		String[] ss = name.split("_");
		for (String s : ss) {
			s = s.substring(0, 1).toUpperCase() + s.substring(1);
			newName = newName + s;
		}
		return newName;
	}
}
