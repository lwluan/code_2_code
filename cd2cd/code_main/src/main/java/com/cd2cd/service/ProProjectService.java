package com.cd2cd.service;

import java.util.List;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProModuleVo;
import com.cd2cd.vo.ProProjectVo;

public interface ProProjectService {

	BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage, Integer pageSize, ProProjectVo proProjectVo);

	ProProjectVo detail(Long id);

	boolean del(Long projectId);

	ServiceCode add(ProProjectVo proProjectVo);

	ServiceCode modify(ProProjectVo proProjectVo);

	BaseRes<String> genProject(Long id);

	BaseRes<DataPageWrapper<ProModuleVo>> moduleList(Integer currPage, Integer pageSize, ProModuleVo proModuleVo);

	ProModuleVo moduleDetail(Long id);

	boolean delModule(Long id);

	ServiceCode addModule(ProModuleVo proModuleVo);

	ServiceCode modifyModule(ProModuleVo proModuleVo);

	/**
	 * 所有项目列表
	 * @return
	 */
	BaseRes<List<ProProjectVo>> projectList();

	BaseRes<List<ProModuleVo>> projectModuleList(Long projectId);
	
}
