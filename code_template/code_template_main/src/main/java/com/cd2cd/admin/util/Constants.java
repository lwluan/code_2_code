package com.cd2cd.admin.util;

public class Constants {

	public static final String ADMIN_NAME = "admin";
	public static final String ROLE_PREFIX = "ROLE_";
	
	/**
	 * 用户登录类型； 管理后台或api登录
	 */
	public enum LoginUserType {
		admin, api, channel
	}
}
