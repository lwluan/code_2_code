package com.cd2cd.admin.service;

import java.util.List;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.ObjDataWrapper;
import com.cd2cd.admin.vo.SysAuthorityVo;
import com.cd2cd.admin.vo.SysRoleVo;

public interface SysRoleService {
	
	BaseRes<DataPageWrapper<SysRoleVo>> list(Integer currPage, Integer pageSize, SysRoleVo sysUserVo);
	
	ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object> detail(Integer id);
	
	boolean del(Integer userId);
	
	ServiceCode add(SysRoleVo sysUserVo);
	
	ServiceCode modify(SysRoleVo sysUserVo);
	
}
