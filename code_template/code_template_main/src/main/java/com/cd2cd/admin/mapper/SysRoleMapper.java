package com.cd2cd.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cd2cd.admin.domain.SysRole;
import com.cd2cd.admin.mapper.gen.SuperSysRoleMapper;

@Mapper
public interface SysRoleMapper extends SuperSysRoleMapper {
	
	List<SysRole> querySysUserRoles(Integer roleId);
	
}