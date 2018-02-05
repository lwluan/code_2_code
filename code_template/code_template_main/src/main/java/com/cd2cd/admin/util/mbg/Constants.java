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
    	try {
			Field field = obj.getClass().getDeclaredField(filedName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
