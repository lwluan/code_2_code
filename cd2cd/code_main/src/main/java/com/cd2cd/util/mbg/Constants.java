package com.cd2cd.util.mbg;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Constants {

	public static class FileType {
		public static final String VO = "vo";
		public static final String Controller = "controller";
		public static final String Service = "service";
		public static final String Dao = "dao";
		public static final String Domain = "domain";
	}
	/**
	 * 忽略以下表和VO自动生成和，创建
	 */
	public static final String[] IGNORE_TABLES = new String[] { "sys_authority", "sys_authority_role_rel", "sys_role",
			"sys_user", "sys_user_role_rel" };

	/**
	 * 默认值为以下list中时，不添加单引号
	 */
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
		Field field = null;
    	try {
    		field = obj.getClass().getDeclaredField(filedName);
			
		} catch (Exception e) {
			try {
				field = obj.getClass().getSuperclass().getDeclaredField(filedName);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
    	
    	field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
