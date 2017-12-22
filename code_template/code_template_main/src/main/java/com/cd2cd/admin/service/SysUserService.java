package com.cd2cd.admin.service;

import java.util.List;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.ObjDataWrapper;
import com.cd2cd.admin.vo.SysRoleVo;
import com.cd2cd.admin.vo.SysUserVo;

public interface SysUserService {
	
	BaseRes<DataPageWrapper<SysUserVo>> list(Integer currPage, Integer pageSize, SysUserVo sysUserVo);
	
	ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object> detail(Integer id);
	
	boolean del(Integer userId);
	
	ServiceCode add(SysUserVo sysUserVo);
	
	ServiceCode modify(SysUserVo sysUserVo);
	
}
