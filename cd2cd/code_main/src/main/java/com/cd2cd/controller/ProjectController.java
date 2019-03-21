package com.cd2cd.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.CommValidate;
import com.cd2cd.domain.ProFunArg;
import com.cd2cd.service.ProjectService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFieldVo;
import com.cd2cd.vo.ProFileVo;
import com.cd2cd.vo.ProFunArgVo;
import com.cd2cd.vo.ProFunVo;
import com.cd2cd.vo.ProPageVo;
import com.cd2cd.vo.ProTableVo;

/**
 * 项目管理
 * @author leiwuluan
 */
@RestController
@RequestMapping("project")
public class ProjectController extends BaseController {
	
	private static Logger LOG = LoggerFactory.getLogger(ProjectController.class);
	
	@Resource
	private ProjectService projectService;
	
	/** database table columns */
	@RequestMapping(value = "tableAndColumns", method = RequestMethod.GET)
	public BaseRes<ProTableVo> fetchTableHasColumnsByTableId(Long tableId) {
		LOG.info("projectId={}", tableId);
		
		return projectService.fetchColumnsByTableId(tableId);
	}
	
	
	
	
	@RequestMapping("fetchTableListByProjectHasDb")
	public BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId) {
		LOG.info("projectId={}", projectId);
		return projectService.fetchTableListByProjectHasDb(projectId);
	}	
	
	
	
	@RequestMapping(value = "tableList", method = RequestMethod.GET)
	public BaseRes<List<ProTableVo>> fetchAllTablesByProject(Long projectId) {
		return projectService.fetchAllTablesByProject(projectId);
	}
	
	/**
	 * ------------------ file 
	 */
	/**
	 * 获取项目树结构
	 * @param projectId: 项目ID
	 * @param packageType: Flat \ Hierachical
	 * @param moduleId: 只显示对应模块
	 */
	@RequestMapping("fileTree")
	public BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId) {
		LOG.info("packageType={}, moduleId={}", packageType, moduleId);
		return projectService.fetchProjectFileTree(projectId, packageType, moduleId);
	}
	
	@RequestMapping(value = "fileList", method = RequestMethod.GET)
	public BaseRes<List<ProFileVo>> fetchFileByClassType(Long projectId, String fileType) {
		return projectService.fetchFileByClassType(projectId, fileType);
	}
	
	@RequestMapping(value = "fileInfo", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "fileInfo", method = RequestMethod.PUT)
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
	@RequestMapping(value = "fileInfo", method = RequestMethod.GET)
	public BaseRes<ProFileVo> fetchFileInfo(Long fileId) {
		BaseRes<ProFileVo> res = projectService.fetchFileInfo(fileId);
		return res;
	}
	
	@RequestMapping(value = "fileInfo", method = RequestMethod.DELETE)
	public BaseRes<String> delFileById(Long fileId) {
		return projectService.delFileById(fileId);
	}
	
	@RequestMapping(value = "fileListWhitField", method = RequestMethod.GET)
	public BaseRes<ProFileVo> fetchFileWithFieldByVoId(Long id) {
		return projectService.fetchFileWithFieldByVoId(id);
	}
	
	
	
	/**
	 * ------------------ file field 
	 */

	@RequestMapping(value = "fileFieldInfo", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "fileFieldInfo", method = RequestMethod.PUT)
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
	
	@RequestMapping(value = "fileFieldInfo", method = RequestMethod.DELETE)
	public BaseRes<String> delFieldFromFile(Long id) {
		return projectService.delFieldFromFile(id);
	}
	
	
	
	/**
	 * ------------------ function
	 */
	@RequestMapping(value = "functionInfo", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "functionInfo", method = RequestMethod.PUT)
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
	
	@RequestMapping(value = "functionInfo", method = RequestMethod.DELETE)
	public BaseRes<String> deleteFunctionByFunId(Long funId) {
		return projectService.deleteFunctionByFunId(funId);
	}
	
	@RequestMapping(value = "fetchFunsByFileId", method = RequestMethod.GET)
	public BaseRes<List<ProFunVo>> fetchFunsByFileId(Long fileId) {
		return projectService.fetchFunsByFileId(fileId);
	}
	
	/**
	 * page
	 **/
	
	@RequestMapping(value = "fetchAllPageByProjectId", method = RequestMethod.GET)
	public BaseRes<List<ProPageVo>> fetchAllPageByProjectId(Long projectId) {
		return projectService.fetchAllPageByProjectId(projectId);
	}
	
	/**
	 * ------------------ funArgs 
	 */
	@RequestMapping(value = "listFunArg", method = RequestMethod.GET)
	public BaseRes<List<ProFunArg>> listFunArg(Long funId) {
		return projectService.listFunArg(funId);
	}
	
	@RequestMapping(value = "addFunArgByFieldId", method = RequestMethod.POST)
	public BaseRes<String> addFunArgByFieldId( 
			@Validated({ProFunArgVo.addByFieldId.class}) @RequestBody
			ProFunArgVo proFunArg, BindingResult bindingResult) {
		return projectService.addFunArg(proFunArg);
	}
	
	@RequestMapping(value = "funArg", method = RequestMethod.POST)
	public BaseRes<String> addFunArg( 
			@Validated({add.class}) @RequestBody
			ProFunArgVo proFunArg, BindingResult bindingResult) {
		return projectService.addFunArg(proFunArg);
	}
	
	@RequestMapping(value = "funArg", method = RequestMethod.PUT)
	public BaseRes<String> modifyFunArg( 
			@Validated({modify.class}) @RequestBody
			ProFunArgVo proFunArg, BindingResult bindingResult) {
		return projectService.modifyFunArg(proFunArg);
	}
	
	@RequestMapping(value = "funArg", method = RequestMethod.DELETE)
	public BaseRes<String> deleteFunArg(Long argId) {
		return projectService.deleteFunArg(argId);
	}
	
	/**
	 * ------------------ validate
	 */
	@RequestMapping(value = "validateList", method = RequestMethod.GET)
	public BaseRes<List<CommValidate>> validateList(Long proId) {
		return projectService.validateList(proId);
	}
	
	/**
	 * ------------------ fun return vo
	 */
	// fetch return vo or page
	
	
}
