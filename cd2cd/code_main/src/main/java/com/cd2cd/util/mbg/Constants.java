package com.cd2cd.util.mbg;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Constants {

	/**
	 * 忽略以下表和VO自动生成和，创建
	 */
	public static final String[] IGNORE_TABLES = new String[] { "sys_authority", "sys_authority_role_rel", "sys_role",
			"sys_user", "sys_user_role_rel" };

	public static final Set<String> DEFAULT_NO_STRING = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("CURRENT_TIMESTAMP");
		}
	};

	public static final String lineSeparator;
	static {
		String ls = System.getProperty("line.separator"); //$NON-NLS-1$
		if (ls == null) {
			ls = "\n"; //$NON-NLS-1$
		}
		lineSeparator = ls;
	}

	@SuppressWarnings("unchecked")
	public static <E> E getObjVal(Object obj, String filedName) {
		try {
			Field field = obj.getClass().getDeclaredField(filedName);
			field.setAccessible(true);
			return (E) field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setObjVal(Object obj, String filedName, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(filedName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
