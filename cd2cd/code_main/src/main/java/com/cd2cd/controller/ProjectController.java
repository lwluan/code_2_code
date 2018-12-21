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

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.service.ProjectService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFieldVo;
import com.cd2cd.vo.ProFileVo;
import com.cd2cd.vo.ProFunVo;
import com.cd2cd.vo.ProPageVo;
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
	@ResponseBody
	@RequestMapping("fetchProjectFileTree")
	public BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId) {
		LOG.info("packageType={}, moduleId={}", packageType, moduleId);
		return projectService.fetchProjectFileTree(projectId, packageType, moduleId);
	}

	@ResponseBody
	@RequestMapping("fetchTableListByProjectHasDb")
	public BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId) {
		LOG.info("projectId={}", projectId);
		return projectService.fetchTableListByProjectHasDb(projectId);
	}	
	
	/**
	 * fetch column from table by id
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("fetchTableHasColumnsByTableId")
	public BaseRes<ProTableVo> fetchTableHasColumnsByTableId(Long tableId) {
		LOG.info("projectId={}", tableId);
		
		// TODO 
		return projectService.fetchColumnsByTableId(tableId);
	}
	
	/**
	 * 添加文件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addFile", method = RequestMethod.POST)
	public BaseRes<String> addFile(
			@Validated 
			@RequestBody ProFileVo proFileVo,
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
		
		return projectService.addFile(proFileVo);
		
	}
	
	/**
	 * modify file info / name comment 
	 * @param proFileVo
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "modifyFileInfo", method = RequestMethod.POST)
	public BaseRes<ProFileVo> modifyFileInfo(
			@Validated 
			@RequestBody ProFileVo proFileVo,
			BindingResult bindingResult) {
		
		BaseRes<ProFileVo> res = new BaseRes<ProFileVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
		
		return projectService.modifyFileInfo(proFileVo);
	}
	
	@ResponseBody
	@RequestMapping(value = "fetchFileByClassType", method = RequestMethod.GET)
	public BaseRes<List<ProFileVo>> fetchFileByClassType(Long projectId, String fileType) {
		return projectService.fetchFileByClassType(projectId, fileType);
	}
	
	@ResponseBody
	@RequestMapping(value = "fetchAllTablesByProject", method = RequestMethod.GET)
	public BaseRes<List<ProTableVo>> fetchAllTablesByProject(Long projectId) {
		return projectService.fetchAllTablesByProject(projectId);
	}
	
	@ResponseBody
	@RequestMapping(value = "fetchFileInfo", method = RequestMethod.GET)
	public BaseRes<ProFileVo> fetchFileInfo(Long fileId) {
		BaseRes<ProFileVo> res = projectService.fetchFileInfo(fileId);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value = "saveFieldToFile", method = RequestMethod.POST)
	public BaseRes<ProFieldVo> saveFieldToFile(
			@Validated 
			@RequestBody ProFieldVo fieldVo,
			BindingResult bindingResult) {
		
		BaseRes<ProFieldVo> res = new BaseRes<ProFieldVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
	
		return projectService.saveOrUpdateFieldToFile(fieldVo);
	}
	
	@ResponseBody
	@RequestMapping(value = "updateFieldToFile", method = RequestMethod.POST)
	public BaseRes<ProFieldVo> updateFieldToFile(
			@Validated 
			@RequestBody ProFieldVo fieldVo,
			BindingResult bindingResult) {
		
		BaseRes<ProFieldVo> res = new BaseRes<ProFieldVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
	
		return projectService.saveOrUpdateFieldToFile(fieldVo);
	}
	
	@ResponseBody
	@RequestMapping(value = "delFieldFromFile", method = RequestMethod.GET)
	public BaseRes<String> delFieldFromFile(Long id) {
		return projectService.delFieldFromFile(id);
	}

	@ResponseBody
	@RequestMapping(value = "fetchFileWithFieldByVoId", method = RequestMethod.GET)
	public BaseRes<ProFileVo> fetchFileWithFieldByVoId(Long id) {
		return projectService.fetchFileWithFieldByVoId(id);
	}

	
	/**
	 * 删除文件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delFileById", method = RequestMethod.GET)
	public BaseRes<String> delFileById(Long fileId) {
		return projectService.delFileById(fileId);
	}
	
	@ResponseBody
	@RequestMapping(value = "addFunction", method = RequestMethod.POST)
	public BaseRes<ProFunVo> addFunction(
			@Validated 
			@RequestBody ProFunVo proFunVo,
			BindingResult bindingResult ) {
		
		BaseRes<ProFunVo> res = new BaseRes<ProFunVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
		return projectService.addFunction(proFunVo);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "modifyFunction", method = RequestMethod.POST)
	public BaseRes<ProFunVo> modifyFunction(
			@Validated 
			@RequestBody ProFunVo proFunVo,
			BindingResult bindingResult ) {
		
		BaseRes<ProFunVo> res = new BaseRes<ProFunVo>();
		if( bindingResult.hasErrors() ) {
			res.setServiceCode(ServiceCode.INVALID_PARAMS);
			return res;
		} 
		return projectService.modifyFunction(proFunVo);
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteFunctionByFunId", method = RequestMethod.GET)
	public BaseRes<String> deleteFunctionByFunId(Long funId) {
		return projectService.deleteFunctionByFunId(funId);
	}
	
	@ResponseBody
	@RequestMapping(value = "fetchFunsByFileId", method = RequestMethod.GET)
	public BaseRes<List<ProFunVo>> fetchFunsByFileId(Long fileId) {
		return projectService.fetchFunsByFileId(fileId);
	}
	
	@ResponseBody
	@RequestMapping(value = "fetchAllPageByProjectId", method = RequestMethod.GET)
	public BaseRes<List<ProPageVo>> fetchAllPageByProjectId(Long projectId) {
		return projectService.fetchAllPageByProjectId(projectId);
	}
	
	
	// fetch return vo or page
	
	
}
