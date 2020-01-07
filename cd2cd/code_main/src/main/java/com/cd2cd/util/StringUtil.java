package com.cd2cd.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {

	/** 首字母大小 */
	public static String firstUpCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public static String firstLowCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
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
			newName = newName + firstUpCase(s);
		}
		ss = newName.split("-");
		newName = "";
		for (String s : ss) {
			newName = newName + firstUpCase(s);
		}
		return newName;
	}

	public static void main(String args[]) {
		System.out.println(getJavaTableName("e_aa_baa-jfkd"));
	}

    public static String clearChar(String dbName) {
		return dbName.replaceAll("-", "").replaceAll("_", "");
    }
}
