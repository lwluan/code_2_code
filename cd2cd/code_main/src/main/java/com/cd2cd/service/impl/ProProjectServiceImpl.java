package com.cd2cd.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.gen.ProProjectCriteria;
import com.cd2cd.domain.gen.ProProjectCriteria.Criteria;
import com.cd2cd.mapper.ProProjectMapper;
import com.cd2cd.service.ProProjectService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProProjectVo;

@Service
public class ProProjectServiceImpl implements ProProjectService {

	@Autowired
	private ProProjectMapper proProjectMapper;
	
	@Override
	public BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage,
			Integer pageSize, ProProjectVo proProjectVo) {
		
		BaseRes<DataPageWrapper<ProProjectVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);
		
		ProProjectCriteria example = new ProProjectCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;
		
		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);
		
		Criteria mCriteria = example.createCriteria();
		if( StringUtils.isNotEmpty(proProjectVo.getName()) ) {
			mCriteria.andNameLike(proProjectVo.getName() + "%");
		}
		
		long totalCount = proProjectMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if( totalCount > 0 ) {
			List<ProProject> proProjectList = proProjectMapper.selectByExample(example);
			List<ProProjectVo> rows = BeanUtil.voConvertList(proProjectList, ProProjectVo.class);
			res.getData().setRows(rows);
		} 
		return res;
	}

	@Override
	public ProProjectVo detail(Long id) {
		ProProject mProProject = proProjectMapper.selectByPrimaryKey(id);
		if( mProProject != null ) {
			ProProjectVo mProProjectVo = BeanUtil.voConvert(mProProject, ProProjectVo.class);
			return mProProjectVo;
		}
		return null;
	}

	@Override
	public boolean del(Long projectId) {
		return proProjectMapper.deleteByPrimaryKey(projectId) > 0;
	}

	@Override
	public ServiceCode add(ProProjectVo proProjectVo) {
		ProProject proProject = BeanUtil.voConvert(proProjectVo, ProProject.class);
		proProject.setCreateTime(new Date());
		proProject.setUpdateTime(new Date());
		
		proProjectMapper.insertSelective(proProject);
		return ServiceCode.SUCCESS;
	}

	@Override
	public ServiceCode modify(ProProjectVo proProjectVo) {
		ProProject proProject = BeanUtil.voConvert(proProjectVo, ProProject.class);
		proProject.setUpdateTime(new Date());
		proProjectMapper.updateByPrimaryKeySelective(proProject);
		return ServiceCode.SUCCESS;
	}

}
