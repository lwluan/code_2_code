package com.cd2cd.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.domain.SysUser;
import com.cd2cd.admin.domain.SysUserRoleRel;
import com.cd2cd.admin.domain.gen.SysUserCriteria;
import com.cd2cd.admin.domain.gen.SysUserCriteria.Criteria;
import com.cd2cd.admin.domain.gen.SysUserRoleRelCriteria;
import com.cd2cd.admin.mapper.SysRoleMapper;
import com.cd2cd.admin.mapper.SysUserMapper;
import com.cd2cd.admin.mapper.SysUserRoleRelMapper;
import com.cd2cd.admin.service.SysUserService;
import com.cd2cd.admin.util.BeanUtil;
import com.cd2cd.admin.util.CommUtils;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.PageData;
import com.cd2cd.admin.vo.SysUserVo;

@Service
public class SysUserServiceImpl implements SysUserService {

	private Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysUserRoleRelMapper sysUserRoleRelMapper;
	
	/**
	 * 处理角色关联
	 * @param roles
	 * @param userId
	 */
	private void updateUserRoles(List<Integer> roles, Integer userId) {
		
		if(null == roles) return;
		
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

	@Override
	public BaseRes<PageData<SysUser>> entityPage(SysUserVo sysUserVo) {
		log.info("sysUserVo={}", JSONObject.toJSONString(sysUserVo));
		BaseRes<PageData<SysUser>> res = new BaseRes<>(ServiceCode.SUCCESS);
		res.setData(new PageData<SysUser>());
		
		Integer current = sysUserVo.getCurrent();
		current = current == null ? 1 : current;
		Integer pageSize = sysUserVo.getPageSize();
		pageSize = pageSize == null ? 12 : pageSize;
		
		res.getData().setCurrent(current);
		res.getData().setPageSize(pageSize);
		
		int mysqlLength = pageSize;
		int mysqlOffset = (current - 1) * mysqlLength;
		
		SysUserCriteria example = new SysUserCriteria();
		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);
		
		Criteria mCriteria = example.createCriteria();
		if( StringUtils.isNotEmpty(sysUserVo.getUsername()) ) {
			mCriteria.andUsernameLike(sysUserVo.getUsername() + "%");
		}
		if( StringUtils.isNotEmpty(sysUserVo.getStatus()) ) {
			mCriteria.andStatusEqualTo(sysUserVo.getStatus());
		}
		
		long total = sysUserMapper.countByExample(example);
		res.getData().setTotal(total);
		if( total > 0 ) {
			List<SysUser> list = sysUserMapper.selectByExample(example);
			List<SysUser> rows = BeanUtil.voConvertListIgnore(list, SysUser.class, "password");
			res.getData().setRows(rows);
		} 
		return res;
	}

	@Override
	public BaseRes<SysUser> entityInfo(Integer id) {
		
		BaseRes<SysUser> res = new BaseRes<>(ServiceCode.SUCCESS);
		SysUser mSysUser = sysUserMapper.selectByPrimaryKey(id);
		mSysUser.setPassword(null);
		
		List<Integer> roles = new ArrayList<>();
		
		SysUserRoleRelCriteria criteria = new SysUserRoleRelCriteria();
		criteria.createCriteria()
		.andUserIdEqualTo(id);
		List<SysUserRoleRel> userRoles = sysUserRoleRelMapper.selectByExample(criteria);
		for(SysUserRoleRel uRole :userRoles) {
			roles.add(uRole.getRoleId());
		}
		mSysUser.setRoles(roles);
		res.setData(mSysUser);
		return res;
	}

	@Override
	public BaseRes<String> deleteEntity(Integer id) {
		sysUserMapper.deleteByPrimaryKey(id);
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> addEntityInfo(SysUserVo sysUserVo) {
		sysUserVo.setCreateTime(new Date());
		sysUserVo.setUpdateTime(new Date());
		
		String password = CommUtils.md5Encode(sysUserVo.getUsername()+sysUserVo.getPassword());
		sysUserVo.setPassword(password);
		sysUserMapper.insertSelective(sysUserVo);
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> modifyEntityInfo(SysUserVo sysUserVo) {
		
		sysUserVo.setUpdateTime(new Date());
		
		String password = sysUserVo.getPassword();
		if(StringUtils.isNotBlank(password)) {
			password = CommUtils.md5Encode(sysUserVo.getUsername()+password);
			sysUserVo.setPassword(password);
		}
		
		sysUserMapper.updateByPrimaryKeySelective(sysUserVo);
		updateUserRoles(sysUserVo.getRoles(), sysUserVo.getId());
		
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

}
