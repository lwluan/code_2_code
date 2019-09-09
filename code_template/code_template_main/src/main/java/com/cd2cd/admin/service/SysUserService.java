package com.cd2cd.admin.service;

import com.cd2cd.admin.domain.SysUser;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.PageData;
import com.cd2cd.admin.vo.SysUserVo;

public interface SysUserService {
	
	BaseRes<PageData<SysUser>> entityPage(SysUserVo sysUserVo);

	BaseRes<SysUser> entityInfo(Integer id);

	BaseRes<String> deleteEntity(Integer id);

	BaseRes<String> addEntityInfo(SysUserVo sysUserVo);

	BaseRes<String> modifyEntityInfo(SysUserVo sysUserVo);
	
}
