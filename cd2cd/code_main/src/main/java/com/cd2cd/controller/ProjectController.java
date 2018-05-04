package com.cd2cd.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd2cd.service.ProjectService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFileVo;

/**
 * 项目管理
 * @author leiwuluan
 */
@Controller
@RequestMapping("project")
public class ProjectController {
	
	private static Logger LOG = LoggerFactory.getLogger(ProjectController.class);
	
	@Resource
	private ProjectService projectService;
	
	/**
	 * 获取项目树结构
	 * @param projectId: 项目ID
	 * @param packageType: Flat \ Hierachical
	 * @param moduleId: 只显示对应模块
	 */
	@RequestMapping("fetchProjectFileTree")
	public @ResponseBody BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId) {
		LOG.info("packageType={}, moduleId={}", packageType, moduleId);
		return projectService.fetchProjectFileTree(projectId, packageType, moduleId);
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
		/**
		 * TODO 
		 */
		return null;
	}
	
	
	
	
}
