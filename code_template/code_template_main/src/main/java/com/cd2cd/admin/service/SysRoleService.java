package com.cd2cd.admin.service;

import java.util.List;

import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.domain.SysRole;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.PageData;
import com.cd2cd.admin.vo.SysRoleVo;

public interface SysRoleService {

	BaseRes<PageData<SysRole>> entityPage(SysRoleVo sysUserVo);

	BaseRes<SysRole> entityInfo(Integer id);

	BaseRes<String> deleteEntity(Integer id);

	BaseRes<String> addEntityInfo(SysRoleVo sysUserVo);

	BaseRes<String> modifyEntityInfo(SysRoleVo sysUserVo);

	BaseRes<List<SysAuthority>> queryAllAuthoritys();
	
}
