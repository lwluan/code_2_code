package com.cd2cd.util;

/**
 * standard:	artifactId.aa.controller.aax_c_x <br>
 * 			 	artifactId.aa.service.aax_s_x <br>
 * 				artifactId.bb.controller.bbx_c_x <br>
 * 			 	artifactId.bb.service.bbx_s_x <br>
 * module: <br>
 * 				artifactId.controller.aax_c_x <br>
 * 								    -.bbx_c_x <br>
 * 				artifactId.service.aax_s_x <br>
 *								 -.bbx_s_x <br>
 * @author leiwuluan
 *
 */
public enum ProjectModuleTypeEnum {

	standard("标准"), module("模块");
	public String msg;

	ProjectModuleTypeEnum(String msg) {
		this.msg = msg;
	}

}
