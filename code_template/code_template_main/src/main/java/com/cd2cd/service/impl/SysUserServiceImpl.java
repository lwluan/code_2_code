package com.cd2cd.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.domain.SysRole;
import com.cd2cd.domain.SysUser;
import com.cd2cd.domain.gen.SysUserCriteria;
import com.cd2cd.domain.gen.SysUserCriteria.Criteria;
import com.cd2cd.mapper.SysRoleMapper;
import com.cd2cd.mapper.SysUserMapper;
import com.cd2cd.service.SysUserService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysUserVo;

@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Override
	public BaseRes<DataPageWrapper<SysUserVo>> list(
			Integer currPage, Integer pageSize, SysUserVo sysUserVo) {
		
		BaseRes<DataPageWrapper<SysUserVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);
		
		SysUserCriteria example = new SysUserCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;
		
		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);
		
		Criteria mCriteria = example.createCriteria();
		if( StringUtils.isNotEmpty(sysUserVo.getUsername()) ) {
			mCriteria.andUsernameLike(sysUserVo.getUsername() + "%");
		}
		
		long totalCount = sysUserMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if( totalCount > 0 ) {
			List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
			List<SysUserVo> rows = BeanUtil.voConvertList(sysUserList, SysUserVo.class);
			res.getData().setRows(rows);
		} 
		return res;
	}

	@Override
	public ObjDataWrapper<SysUserVo, List<SysRole>, Object> detail(Integer userId) {
		
		ObjDataWrapper<SysUserVo, List<SysRole>, Object> objDataWrap = new ObjDataWrapper<SysUserVo, List<SysRole>, Object>();
		
		SysUser mSysUser = sysUserMapper.selectByPrimaryKey(userId);
		List<SysRole> sysRoles = sysRoleMapper.querySysUserRoles(userId);
		
		SysUserVo data01 = BeanUtil.voConvert(mSysUser, SysUserVo.class);
		List<SysRole> data02 = BeanUtil.voConvertList(sysRoles, SysRole.class); 
		objDataWrap.setData1(data01);
		objDataWrap.setData2(data02);
		
		return objDataWrap;
	}

	@Override
	public boolean del(Integer userId) {
		int affected = sysRoleMapper.deleteByPrimaryKey(userId);
		return (affected > 0 ? true : false);
	}

}
