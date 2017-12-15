package com.cd2cd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cd2cd.domain.SysAuthority;
import com.cd2cd.mapper.gen.SuperSysAuthorityMapper;

@Mapper
public interface SysAuthorityMapper extends SuperSysAuthorityMapper {
	
	List<SysAuthority> querySysRoleAuthorities(Integer roleId);

	List<SysAuthority> userAuthority(Integer userId);
	
}