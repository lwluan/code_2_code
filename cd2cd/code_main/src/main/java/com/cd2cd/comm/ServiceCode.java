package com.cd2cd.comm;


/**
 * 业务返回码 <br>
 * 请勿修改 GEN_CODE_START ... GEN_CODE_END 标签和标签之间的内容
 */
public enum ServiceCode {
	___GEN_CODE_START___(-20000, "自动生成内容【开始】不要编辑"),
	SUCCESS(10000, "成功"),
	FAILED(10001, "失败"),
	ACCESS_DENIED(10003, "无权限操作"),
	___GEN_CODE_END___(-10000, "自动生成内容【结束】不要编辑"),
	
	;
	
	
	public String msg;
	public int code;
	
	ServiceCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
