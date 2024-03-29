package com.cd2cd.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.SysRole;
import com.cd2cd.domain.SysUser;
import com.cd2cd.domain.SysUserRoleRel;
import com.cd2cd.domain.gen.SysUserCriteria;
import com.cd2cd.domain.gen.SysUserCriteria.Criteria;
import com.cd2cd.domain.gen.SysUserRoleRelCriteria;
import com.cd2cd.mapper.SysRoleMapper;
import com.cd2cd.mapper.SysUserMapper;
import com.cd2cd.mapper.SysUserRoleRelMapper;
import com.cd2cd.security.Md5PasswordEncoder;
import com.cd2cd.service.SysUserService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysRoleVo;
import com.cd2cd.vo.SysUserVo;

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
		
		BaseRes<DataPageWrapper<SysUserVo>> res = new BaseRes<>();
		res.setData(new DataPageWrapper<SysUserVo>());
		res.getData().setCurrPage(currPage);
		res.getData().setPageSize(pageSize);
		
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
	public ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object> detail(Integer userId) {
		
		ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object> objDataWrap = new ObjDataWrapper<SysUserVo, List<SysRoleVo>, Object>();
		
		SysUser mSysUser = sysUserMapper.selectByPrimaryKey(userId);
		List<SysRole> sysRoles = sysRoleMapper.querySysUserRoles(userId);
		
		SysUserVo data01 = new SysUserVo();
		if( mSysUser != null ) {
			data01 = BeanUtil.voConvert(mSysUser, SysUserVo.class);
			data01.setPassword(null);
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
