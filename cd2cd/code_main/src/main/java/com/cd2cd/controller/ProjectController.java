package com.cd2cd.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd2cd.comm.DValid;
import com.cd2cd.comm.ServiceCode;
import com.cd2cd.service.ProjectService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFieldVo;
import com.cd2cd.vo.ProFileVo;
import com.cd2cd.vo.ProTableVo;

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
	
	@RequestMapping("fetchTableListByProjectHasDb")
	public @ResponseBody BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId) {
		LOG.info("projectId={}", projectId);
		return projectService.fetchTableListByProjectHasDb(projectId);
	}	
	
	/**
	 * 添加文件
	 * @return
	 */
	@RequestMapping(value = "addFile", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> addFile(
			@Validated(value = {DValid.AddEntity.class}) 
			@RequestBody ProFileVo proFileVo,
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
		
		return projectService.addFile(proFileVo);
		
	}
	
	@RequestMapping(value = "fetchFileInfo", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProFileVo> fetchFileInfo(Long fileId) {
		
		BaseRes<ProFileVo> res = projectService.fetchFileInfo(fileId);
		
		return res;
	}
	
	@RequestMapping(value = "saveFieldToFile", method = RequestMethod.POST)
	public @ResponseBody BaseRes<ProFieldVo> saveFieldToFile(
			@Validated(value = {DValid.AddEntity.class}) 
			@RequestBody ProFieldVo fieldVo,
			BindingResult bindingResult) {
		
		BaseRes<ProFieldVo> res = new BaseRes<ProFieldVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
	
		return projectService.saveOrUpdateFieldToFile(fieldVo);
	}
	
	@RequestMapping(value = "updateFieldToFile", method = RequestMethod.POST)
	public @ResponseBody BaseRes<ProFieldVo> updateFieldToFile(
			@Validated(value = {DValid.ModifyEntity.class}) 
			@RequestBody ProFieldVo fieldVo,
			BindingResult bindingResult) {
		
		BaseRes<ProFieldVo> res = new BaseRes<ProFieldVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
	
		return projectService.saveOrUpdateFieldToFile(fieldVo);
	}
	
	@RequestMapping(value = "delFieldFromFile", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> delFieldFromFile(Long id) {
		return projectService.delFieldFromFile(id);
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