package com.cd2cd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cd2cd.domain.SysRole;
import com.cd2cd.mapper.gen.SuperSysRoleMapper;

@Mapper
public interface SysRoleMapper extends SuperSysRoleMapper {
	
	List<SysRole> querySysUserRoles(Integer userId);
	
}