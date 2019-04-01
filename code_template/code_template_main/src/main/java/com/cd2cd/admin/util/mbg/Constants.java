package com.cd2cd.admin.util.mbg;

import java.lang.reflect.Field;

public class Constants {
	
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
