package com.cd2cd.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.SysAuthority;
import com.cd2cd.domain.SysAuthorityRoleRel;
import com.cd2cd.domain.SysRole;
import com.cd2cd.domain.gen.SysAuthorityRoleRelCriteria;
import com.cd2cd.domain.gen.SysRoleCriteria;
import com.cd2cd.domain.gen.SysRoleCriteria.Criteria;
import com.cd2cd.mapper.SysAuthorityMapper;
import com.cd2cd.mapper.SysAuthorityRoleRelMapper;
import com.cd2cd.mapper.SysRoleMapper;
import com.cd2cd.service.SysRoleService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysAuthorityVo;
import com.cd2cd.vo.SysRoleVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;
	
	@Autowired
	private SysAuthorityRoleRelMapper sysAuthorityRoleRelMapper;
	
	@Override
	public BaseRes<DataPageWrapper<SysRoleVo>> list(Integer currPage,
			Integer pageSize, SysRoleVo sysUserVo) {
		
		BaseRes<DataPageWrapper<SysRoleVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);
		
		SysRoleCriteria example = new SysRoleCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;
		
		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);
		
		Criteria mCriteria = example.createCriteria();
		if( StringUtils.isNotEmpty(sysUserVo.getName()) ) {
			mCriteria.andNameLike(sysUserVo.getName() + "%");
		}
		
		long totalCount = sysRoleMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if( totalCount > 0 ) {
			List<SysRole> sysUserList = sysRoleMapper.selectByExample(example);
			List<SysRoleVo> rows = BeanUtil.voConvertList(sysUserList, SysRoleVo.class);
			res.getData().setRows(rows);
		} 
		return res;
	}

	@Override
	public ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object> detail(
			Integer id) {
		ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object> objDataWrap = new ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object>();
		
		SysRole mSysRole = sysRoleMapper.selectByPrimaryKey(id);
		List<SysAuthority> sysRoles = sysAuthorityMapper.querySysRoleAuthorities(id);
		
		SysRoleVo data01 = new SysRoleVo();
		if( null != mSysRole ) {
			data01 = BeanUtil.voConvert(mSysRole, SysRoleVo.class);
		}
		List<SysAuthorityVo> data02 = BeanUtil.voConvertList(sysRoles, SysAuthorityVo.class); 
		objDataWrap.setData1(data01);
		objDataWrap.setData2(data02);
		
		return objDataWrap;
	}

	@Override
	public boolean del(Integer userId) {
		int affected = sysRoleMapper.deleteByPrimaryKey(userId);
		return (affected > 0 ? true : false);
	}

	@Override
	public ServiceCode add(SysRoleVo sysRoleVo) {
		
		SysRole sysRole = BeanUtil.voConvert(sysRoleVo, SysRole.class);
		sysRole.setCreateTime(new Date());
		sysRole.setUpdateTime(new Date());
		sysRoleMapper.insertSelective(sysRole);
		updateRoleAuthorities(sysRoleVo.getAuthIds(), sysRole.getId());
		
		return ServiceCode.SUCCESS;
	}

	@Override
	public ServiceCode modify(SysRoleVo sysRoleVo) {
		
		SysRole sysRole = BeanUtil.voConvert(sysRoleVo, SysRole.class);
		sysRole.setUpdateTime(new Date());
		sysRoleMapper.updateByPrimaryKeySelective(sysRole);
		updateRoleAuthorities(sysRoleVo.getAuthIds(), sysRole.getId());
		
		return ServiceCode.SUCCESS;
	}

	/**
	 * 处理权限关联
	 * @param roles
	 * @param userId
	 */
	private void updateRoleAuthorities(List<String> authIds, Integer roleId) {
		
		SysAuthorityRoleRelCriteria example = new SysAuthorityRoleRelCriteria();
		example.createCriteria().andRoleIdEqualTo(roleId);
		sysAuthorityRoleRelMapper.deleteByExample(example);
		
		for( String authId: authIds ) {
			SysAuthorityRoleRel record = new SysAuthorityRoleRel();
			record.setRoleId(roleId);
			record.setAuthoritiesId(authId);
			sysAuthorityRoleRelMapper.insertSelective(record);
		}
	}

}
