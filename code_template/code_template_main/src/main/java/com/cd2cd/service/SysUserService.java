package com.cd2cd.service;

import java.util.List;

import com.cd2cd.domain.SysRole;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysUserVo;

public interface SysUserService {
	
	BaseRes<DataPageWrapper<SysUserVo>> list(Integer currPage, Integer pageSize, SysUserVo sysUserVo);
	
	ObjDataWrapper<SysUserVo, List<SysRole>, Object> detail(Integer id);
	
	boolean del(Integer userId);
}
