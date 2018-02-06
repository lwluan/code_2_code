package com.cd2cd.service;

import com.cd2cd.vo.BaseRes;

public interface ProjectService {
	
	/**
	 * 获取项目树结构
	 * @param packageType
	 * @param moduleId
	 * @return
	 */
	BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId);
	
}
