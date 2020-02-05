package com.cd2cd.service;

import java.util.List;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysRoleVo;
import com.cd2cd.vo.SysUserVo;

public interface SysUserService {
	
	BaseRes<DataPageWrapper<SysUserVo>> list(Integer currPage, Integer pageSize, SysUserVo sysUserVo);
	
	ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object> detail(Integer id);
	
	boolean del(Integer userId);
	
	ServiceCode add(SysUserVo sysUserVo);
	
	ServiceCode modify(SysUserVo sysUserVo);
	
}
