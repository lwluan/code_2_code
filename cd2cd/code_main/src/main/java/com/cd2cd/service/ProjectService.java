package com.cd2cd.service;

import java.util.List;

import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFieldVo;
import com.cd2cd.vo.ProFileVo;
import com.cd2cd.vo.ProTableVo;

public interface ProjectService {
	
	/**
	 * 获取项目树结构
	 * @param packageType
	 * @param moduleId
	 * @return
	 */
	BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId);

	/**
	 * 获取项目关联的所有数据库表
	 * @param projectId
	 * @return
	 */
	BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId);

	/**
	 * 添加文件
	 * @param proFileVo
	 * @return
	 */
	BaseRes<String> addFile(ProFileVo proFileVo);

	/**
	 * fetch file info that has fields and column if it had
	 * @param fileId
	 * @return
	 */
	BaseRes<ProFileVo> fetchFileInfo(Long fileId);

	/**
	 * update or save 
	 * @param fileVo
	 * @return
	 */
	BaseRes<ProFieldVo> saveOrUpdateFieldToFile(ProFieldVo fileVo);

	/**
	 * delete field from file
	 * @param id
	 * @return
	 */
	BaseRes<String> delFieldFromFile(Long id);

}
