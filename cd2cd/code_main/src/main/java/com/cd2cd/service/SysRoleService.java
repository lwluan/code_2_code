package com.cd2cd.service;

import java.util.List;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysAuthorityVo;
import com.cd2cd.vo.SysRoleVo;

public interface SysRoleService {
	
	BaseRes<DataPageWrapper<SysRoleVo>> list(Integer currPage, Integer pageSize, SysRoleVo sysUserVo);
	
	ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object> detail(Integer id);
	
	boolean del(Integer userId);
	
	ServiceCode add(SysRoleVo sysUserVo);
	
	ServiceCode modify(SysRoleVo sysUserVo);
	
}
