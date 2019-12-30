package com.cd2cd.service;

import java.util.List;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.vo.*;

public interface ProProjectService {

	BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage, Integer pageSize, ProProjectVo proProjectVo);

	ProProjectVo detail(Long id);

	boolean del(Long projectId);

	ServiceCode add(ProProjectVo proProjectVo);

	ServiceCode modify(ProProjectVo proProjectVo);

	BaseRes<String> genProject(ProProjectVo projectVo);

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

	/** 微服务 */
    BaseRes<List<ProMicroServiceVo>> projectMicroServiceList(Long projectId);

	BaseRes<String> delMicroService(Long id);

	BaseRes<String> addMicroService(ProMicroServiceVo proMicroServiceVo);

	BaseRes<String> modifyMicroService(ProMicroServiceVo proMicroServiceVo);
}
