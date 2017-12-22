package com.cd2cd.service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProProjectVo;

public interface ProProjectService {
	
	BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage, Integer pageSize, ProProjectVo proProjectVo);
	
	ProProjectVo detail(Long id);
	
	boolean del(Long projectId);
	
	ServiceCode add(ProProjectVo proProjectVo);
	
	ServiceCode modify(ProProjectVo proProjectVo);
	
	BaseRes<String> genProject(Long id);
}
