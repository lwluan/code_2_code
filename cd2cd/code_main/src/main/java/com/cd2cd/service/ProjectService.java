package com.cd2cd.service;

import java.util.List;

import com.cd2cd.domain.CommValidate;
import com.cd2cd.domain.ProFunArg;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFieldVo;
import com.cd2cd.vo.ProFileVo;
import com.cd2cd.vo.ProFunArgVo;
import com.cd2cd.vo.ProFunVo;
import com.cd2cd.vo.ProPageVo;
import com.cd2cd.vo.ProTableVo;

public interface ProjectService {

	/**
	 * 获取项目树结构
	 * 
	 * @param packageType
	 * @param moduleId
	 * @return
	 */
	BaseRes<String> fetchProjectFileTree(Long projectId, String packageType,
			Long moduleId);

	/**
	 * 获取项目关联的所有数据库表
	 * 
	 * @param projectId
	 * @return
	 */
	BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId, String from);

	/**
	 * 添加文件
	 * 
	 * @param proFileVo
	 * @return
	 */
	BaseRes<String> addFile(ProFileVo proFileVo);

	/**
	 * fetch file info that has fields and column if it had
	 * 
	 * @param fileId
	 * @return
	 */
	BaseRes<ProFileVo> fetchFileInfo(Long fileId);

	/**
	 * update or save
	 * 
	 * @param fileVo
	 * @return
	 */
	BaseRes<ProFieldVo> saveOrUpdateFieldToFile(ProFieldVo fileVo);

	/**
	 * delete field from file
	 * 
	 * @param id
	 * @return
	 */
	BaseRes<String> delFieldFromFile(Long id);

	/**
	 * fetch file of project by class type
	 * @param projectId
	 * @param classType
	 * @return
	 */
	BaseRes<List<ProFileVo>> fetchFileByClassType(Long projectId,
			String fileType);

	/**
	 * modify file info for update
	 * @param proFileVo
	 * @return
	 */
	BaseRes<ProFileVo> modifyFileInfo(ProFileVo proFileVo);

	/**
	 * fetch table of db from db
	 * @param projectId
	 * @return
	 */
	BaseRes<List<ProTableVo>> fetchAllTablesByProject(Long projectId);

	/**
	 * fetch columns by table id
	 * @param projectId
	 * @return
	 */
	BaseRes<ProTableVo> fetchColumnsByTableId(Long tableId);

	BaseRes<String> delFileById(Long fileId);

	BaseRes<List<ProFunVo>> fetchFunsByFileId(Long fileId);

	BaseRes<ProFunVo> addFunction(ProFunVo proFunVo);

	BaseRes<ProFunVo> modifyFunction(ProFunVo proFunVo);

	BaseRes<String> deleteFunctionByFunId(Long funId);

	BaseRes<List<ProPageVo>> fetchAllPageByProjectId(Long projectId);

	/**
	 * 获取vo文件 成员变量列表和文件信息
	 * 返回字段验证规则之类数据
	 * @param id
	 * @return
	 */
	BaseRes<ProFileVo> fetchFileWithFieldByVoId(Long id);

	/**
	 * =============方法参数相关操作
	 * */
	BaseRes<List<ProFunArg>> listFunArg(Long funId);

	BaseRes<String> addFunArg(ProFunArgVo proFunArg);

	BaseRes<String> modifyFunArg(ProFunArgVo proFunArg);

	BaseRes<String> deleteFunArg(Long argId);

	/**
	 * ============= validate
	 */
	BaseRes<List<CommValidate>> validateList(Long proId);

}
