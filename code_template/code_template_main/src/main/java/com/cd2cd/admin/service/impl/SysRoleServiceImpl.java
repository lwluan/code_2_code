package com.cd2cd.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.domain.SysAuthorityRoleRel;
import com.cd2cd.admin.domain.SysRole;
import com.cd2cd.admin.domain.gen.SysAuthorityRoleRelCriteria;
import com.cd2cd.admin.domain.gen.SysRoleCriteria;
import com.cd2cd.admin.domain.gen.SysRoleCriteria.Criteria;
import com.cd2cd.admin.mapper.SysAuthorityMapper;
import com.cd2cd.admin.mapper.SysAuthorityRoleRelMapper;
import com.cd2cd.admin.mapper.SysRoleMapper;
import com.cd2cd.admin.service.SysRoleService;
import com.cd2cd.admin.util.BeanUtil;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.SysRoleVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;
	
	@Autowired
	private SysAuthorityRoleRelMapper sysAuthorityRoleRelMapper;

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

	@Override
	public BaseRes<DataPageWrapper<SysRole>> entityPage(SysRoleVo sysUserVo) {
		BaseRes<DataPageWrapper<SysRole>> res = new BaseRes<>(ServiceCode.SUCCESS);
		res.setData(new DataPageWrapper<SysRole>());
		
		Integer current = sysUserVo.getCurrent();
		current = current == null ? 1 : current;
		Integer pageSize = sysUserVo.getPageSize();
		pageSize = pageSize == null ? 12 : pageSize;
		
		res.getData().setCurrent(current);
		res.getData().setPageSize(pageSize);
		
		int mysqlLength = pageSize;
		int mysqlOffset = (current - 1) * mysqlLength;
		
		SysRoleCriteria example = new SysRoleCriteria();
		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);
		
		Criteria mCriteria = example.createCriteria();
		if( StringUtils.isNotEmpty(sysUserVo.getName()) ) {
			mCriteria.andNameLike(sysUserVo.getName() + "%");
		}
		
		long total = sysRoleMapper.countByExample(example);
		res.getData().setTotal(total);
		if( total > 0 ) {
			List<SysRole> list = sysRoleMapper.selectByExample(example);
			List<SysRole> rows = BeanUtil.voConvertListIgnore(list, SysRole.class, "password");
			res.getData().setRows(rows);
		} 
		return res;
	}

	@Override
	public BaseRes<SysRole> entityInfo(Integer id) {
		
		BaseRes<SysRole> res = new BaseRes<>(ServiceCode.SUCCESS);
		SysRole mSysRole = sysRoleMapper.selectByPrimaryKey(id);
		
		SysAuthorityRoleRelCriteria criteria = new SysAuthorityRoleRelCriteria();
		criteria.createCriteria()
		.andRoleIdEqualTo(id);
		List<SysAuthorityRoleRel> authoritys = sysAuthorityRoleRelMapper.selectByExample(criteria);
		
		List<String> authIds = new ArrayList<>();
		for(SysAuthorityRoleRel auth :authoritys) {
			authIds.add(auth.getAuthoritiesId());
		}
		mSysRole.setAuthIds(authIds);
		res.setData(mSysRole);
		return res;
	}

	@Override
	public BaseRes<String> deleteEntity(Integer id) {
		sysRoleMapper.deleteByPrimaryKey(id);
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> addEntityInfo(SysRoleVo sysUserVo) {
		sysUserVo.setCreateTime(new Date());
		sysUserVo.setUpdateTime(new Date());
		
		updateRoleAuthorities(sysUserVo.getAuthIds(), sysUserVo.getId());
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> modifyEntityInfo(SysRoleVo sysUserVo) {
		
		sysUserVo.setUpdateTime(new Date());
		sysRoleMapper.updateByPrimaryKeySelective(sysUserVo);
		updateRoleAuthorities(sysUserVo.getAuthIds(), sysUserVo.getId());
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<List<SysAuthority>> queryAllAuthoritys() {
		List<SysAuthority> auths = sysAuthorityMapper.selectByExample(null);
		if( ! CollectionUtils.isEmpty(auths)) {
			auths = BeanUtil.voConvertListHasIn(auths, SysAuthority.class, "pid", "guid", "name");
			return new BaseRes<>(auths, ServiceCode.SUCCESS);
		}
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

}
