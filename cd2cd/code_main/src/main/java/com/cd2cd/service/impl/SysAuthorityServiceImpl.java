package com.cd2cd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.domain.SysAuthority;
import com.cd2cd.mapper.SysAuthorityMapper;
import com.cd2cd.service.SysAuthorityService;

@Service
public class SysAuthorityServiceImpl implements SysAuthorityService {

	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;
	
	@Override
	public List<SysAuthority> getAllAuthority() {
		return sysAuthorityMapper.selectByExample(null);
	}

}
