package com.cd2cd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFileVo;

/**
 * 项目管理
 * @author leiwuluan
 */
@Controller
@RequestMapping("project")
public class ProjectController {
	
	/**
	 * 获取项目树结构
	 * @param packageType: Flat \ Hierachical
	 * @param modulId: 只显示对应模块
	 */
	@RequestMapping("fetchProjectFileTree")
	public void fetchProjectFileTree(String packageType, Long modulId) {
		
	}
	
	/**
	 * 添加文件
	 * @return
	 */
	public BaseRes<String> addFile(ProFileVo proFileVo) {
		return null;
	}
	
	/**
	 * 删除文件
	 * @return
	 */
	public BaseRes<String> delFile() {
		return null;
	}
	
	
	
	
}
