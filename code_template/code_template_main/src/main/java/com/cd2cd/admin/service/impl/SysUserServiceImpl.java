package com.cd2cd.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.domain.SysRole;
import com.cd2cd.admin.domain.SysUser;
import com.cd2cd.admin.domain.SysUserRoleRel;
import com.cd2cd.admin.domain.gen.SysUserCriteria;
import com.cd2cd.admin.domain.gen.SysUserCriteria.Criteria;
import com.cd2cd.admin.domain.gen.SysUserRoleRelCriteria;
import com.cd2cd.admin.mapper.SysRoleMapper;
import com.cd2cd.admin.mapper.SysUserMapper;
import com.cd2cd.admin.mapper.SysUserRoleRelMapper;
import com.cd2cd.admin.security.Md5PasswordEncoder;
import com.cd2cd.admin.service.SysUserService;
import com.cd2cd.admin.util.BeanUtil;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.ObjDataWrapper;
import com.cd2cd.admin.vo.SysRoleVo;
import com.cd2cd.admin.vo.SysUserVo;

@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysUserRoleRelMapper sysUserRoleRelMapper;
	
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
		if( StringUtils.isNotEmpty(sysUserVo.getStatus()) ) {
			mCriteria.andStatusEqualTo(sysUserVo.getStatus());
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
	public ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object> detail(Integer userId) {
		
		ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object> objDataWrap = new ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object>();
		
		SysUser mSysUser = sysUserMapper.selectByPrimaryKey(userId);
		if( null == mSysUser ) {
			mSysUser = new SysUser();
		}
		mSysUser.setPassword(null);
		List<SysRole> sysRoles = sysRoleMapper.querySysUserRoles(userId);
		
		SysUserVo data01 = new SysUserVo();
		if( mSysUser != null ) {
			data01 = BeanUtil.voConvert(mSysUser, SysUserVo.class);
		}
		List<SysRoleVo> data02 = BeanUtil.voConvertList(sysRoles, SysRoleVo.class); 
		objDataWrap.setData1(data01);
		objDataWrap.setData2(data02);
		
		return objDataWrap;
	}

	@Override
	public boolean del(Integer userId) {
		int affected = sysUserMapper.deleteByPrimaryKey(userId);
		return (affected > 0 ? true : false);
	}

	@Override
	public ServiceCode add(SysUserVo sysUserVo) {
		
		SysUser sysUser = BeanUtil.voConvert(sysUserVo, SysUser.class);
		sysUser.setCreateTime(new Date());
		sysUser.setUpdateTime(new Date());
		
		// 密码加密处理
		String password = Md5PasswordEncoder.md5Encode(sysUser.getPassword());
		sysUser.setPassword(password);
		
		sysUserMapper.insertSelective(sysUser);
		updateUserRoles(sysUserVo.getRoles(), sysUser.getId());
		
		return ServiceCode.SUCCESS;
	}

	@Override
	public ServiceCode modify(SysUserVo sysUserVo) {
		
		SysUser sysUser = BeanUtil.voConvert(sysUserVo, SysUser.class);
		sysUser.setUpdateTime(new Date());
		
		if( StringUtils.isNotEmpty(sysUser.getPassword()) ) {
			// 密码加密处理
			String password = Md5PasswordEncoder.md5Encode(sysUser.getPassword());
			sysUser.setPassword(password);
		}
		
		sysUserMapper.updateByPrimaryKeySelective(sysUser);
		updateUserRoles(sysUserVo.getRoles(), sysUserVo.getId());
		
		return ServiceCode.SUCCESS;
	}
	
	/**
	 * 处理角色关联
	 * @param roles
	 * @param userId
	 */
	private void updateUserRoles(List<Integer> roles, Integer userId) {
		
		SysUserRoleRelCriteria example = new SysUserRoleRelCriteria();
		example.createCriteria().andUserIdEqualTo(userId);
		sysUserRoleRelMapper.deleteByExample(example);
		
		for( Integer roleId: roles ) {
			SysUserRoleRel record = new SysUserRoleRel();
			record.setRoleId(roleId);
			record.setUserId(userId);
			sysUserRoleRelMapper.insertSelective(record);
		}
	}

}
