package com.cd2cd.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.mapper.gen.SuperSysAuthorityMapper;

@Mapper
public interface SysAuthorityMapper extends SuperSysAuthorityMapper {
	
	List<SysAuthority> querySysRoleAuthorities(Integer roleId);

	List<SysAuthority> userAuthority(Integer userId);
	
}