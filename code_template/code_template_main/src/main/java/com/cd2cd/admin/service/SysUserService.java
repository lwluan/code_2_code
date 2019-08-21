package com.cd2cd.admin.service;

import com.cd2cd.admin.domain.SysUser;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.SysUserVo;

public interface SysUserService {
	
	BaseRes<DataPageWrapper<SysUser>> entityPage(SysUserVo sysUserVo);

	BaseRes<SysUser> entityInfo(Integer id);

	BaseRes<String> deleteEntity(Integer id);

	BaseRes<String> addEntityInfo(SysUserVo sysUserVo);

	BaseRes<String> modifyEntityInfo(SysUserVo sysUserVo);
	
}
