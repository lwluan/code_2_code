package com.cd2cd.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.ProProjectDatabaseRel;
import com.cd2cd.domain.gen.ProModuleCriteria;
import com.cd2cd.domain.gen.ProProjectCriteria;
import com.cd2cd.domain.gen.ProProjectCriteria.Criteria;
import com.cd2cd.domain.gen.ProProjectDatabaseRelCriteria;
import com.cd2cd.mapper.ProModuleMapper;
import com.cd2cd.mapper.ProProjectDatabaseRelMapper;
import com.cd2cd.mapper.ProProjectMapper;
import com.cd2cd.service.ProProjectService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.util.ProjectUtils;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProModuleVo;
import com.cd2cd.vo.ProProjectVo;

@Service
public class ProProjectServiceImpl implements ProProjectService {

	@Autowired
	private ProProjectMapper proProjectMapper;

	@Autowired
	private ProProjectDatabaseRelMapper proProjectDatabaseRelMapper;

	@Autowired
	private ProModuleMapper proModuleMapper;

	@Override
	public BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage, Integer pageSize, ProProjectVo proProjectVo) {

		BaseRes<DataPageWrapper<ProProjectVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);

		ProProjectCriteria example = new ProProjectCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;

		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);

		Criteria mCriteria = example.createCriteria();
		if (StringUtils.isNotEmpty(proProjectVo.getName())) {
			mCriteria.andNameLike(proProjectVo.getName() + "%");
		}

		long totalCount = proProjectMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if (totalCount > 0) {
			List<ProProject> proProjectList = proProjectMapper.selectByExample(example);
			List<ProProjectVo> rows = BeanUtil.voConvertList(proProjectList, ProProjectVo.class);
			res.getData().setRows(rows);
		}
		return res;
	}

	@Override
	public ProProjectVo detail(Long id) {
		ProProject mProProject = proProjectMapper.selectByPrimaryKey(id);
		if (mProProject != null) {
			ProProjectVo mProProjectVo = BeanUtil.voConvert(mProProject, ProProjectVo.class);

			List<Long> dbIds = new ArrayList<Long>();

			// query project ref db list
			ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
			mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(mProProjectVo.getId());
			List<ProProjectDatabaseRel> list = proProjectDatabaseRelMapper.selectByExample(mProProjectDatabaseRelCriteria);
			for (int i = 0; i < list.size(); i++) {
				dbIds.add(list.get(i).getDatabaseId());
			}

			mProProjectVo.setDbIds(dbIds);
			return mProProjectVo;
		}
		return null;
	}

	@Override
	public boolean del(Long projectId) {
		// delete project to db ref
		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		proProjectDatabaseRelMapper.deleteByExample(mProProjectDatabaseRelCriteria);

		// delete module

		// delete project file

		return proProjectMapper.deleteByPrimaryKey(projectId) > 0;
	}

	@Override
	public ServiceCode add(ProProjectVo proProjectVo) {
		ProProject proProject = BeanUtil.voConvert(proProjectVo, ProProject.class);
		proProject.setCreateTime(new Date());
		proProject.setUpdateTime(new Date());

		proProjectMapper.insertSelective(proProject);

		// insert project and database
		if (null != proProjectVo.getDbIds()) {
			updateProjectAndDatabaseRel(proProject.getId(), proProjectVo.getDbIds());
		}

		return ServiceCode.SUCCESS;
	}

	@Override
	public ServiceCode modify(ProProjectVo proProjectVo) {
		ProProject proProject = BeanUtil.voConvert(proProjectVo, ProProject.class);
		proProject.setUpdateTime(new Date());
		proProjectMapper.updateByPrimaryKeySelective(proProject);

		if (null != proProjectVo.getDbIds()) {
			updateProjectAndDatabaseRel(proProject.getId(), proProjectVo.getDbIds());
		}

		return ServiceCode.SUCCESS;
	}

	private void updateProjectAndDatabaseRel(Long projectId, List<Long> dbIds) {

		// remove rel
		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		proProjectDatabaseRelMapper.deleteByExample(mProProjectDatabaseRelCriteria);

		// add rel
		for (Long dbId : dbIds) {
			ProProjectDatabaseRel mProProjectDatabaseRel = new ProProjectDatabaseRel();
			mProProjectDatabaseRel.setCreateTime(new Date());
			mProProjectDatabaseRel.setUpdateTime(new Date());
			mProProjectDatabaseRel.setProjectId(projectId);
			mProProjectDatabaseRel.setDatabaseId(dbId);
			proProjectDatabaseRelMapper.insert(mProProjectDatabaseRel);
		}
	}

	@Override
	public BaseRes<String> genProject(Long id) {

		ProProject mProProject = proProjectMapper.selectByPrimaryKey(id);

		BaseRes<String> res = new BaseRes<String>();

		// copy project to folder
		try {
			ProjectUtils.genProject(mProProject);
			res.setServiceCode(ServiceCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			res.setServiceCode(ServiceCode.FAILED);
			res.setMsg(e.getMessage());
		}
		return res;

		// replace package name from project file, folder

	}

	@Override
	public BaseRes<DataPageWrapper<ProModuleVo>> moduleList(Integer currPage, Integer pageSize, ProModuleVo proModuleVo) {

		BaseRes<DataPageWrapper<ProModuleVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);

		ProModuleCriteria example = new ProModuleCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;

		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);

		ProModuleCriteria.Criteria mCriteria = example.createCriteria();
		if (StringUtils.isNotEmpty(proModuleVo.getName())) {
			mCriteria.andNameLike(proModuleVo.getName() + "%");
		}

		long totalCount = proModuleMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if (totalCount > 0) {
			List<ProModule> proModuleList = proModuleMapper.selectByExample(example);
			List<ProModuleVo> rows = BeanUtil.voConvertList(proModuleList, ProModuleVo.class);
			res.getData().setRows(rows);
		}
		return res;
	}

	@Override
	public ProModuleVo moduleDetail(Long id) {
		ProModule mProModule = proModuleMapper.selectByPrimaryKey(id);
		if (mProModule != null) {
			ProModuleVo mProModuleVo = BeanUtil.voConvert(mProModule, ProModuleVo.class);
			return mProModuleVo;
		}
		return null;
	}

	@Override
	public boolean delModule(Long id) {

		// remove file or module
		
		return 0 < proModuleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ServiceCode addModule(ProModuleVo proModuleVo) {

		proModuleVo.setCreateTime(new Date());
		proModuleVo.setUpdateTime(new Date());

		return 0 < proModuleMapper.insertSelective(proModuleVo) ? ServiceCode.SUCCESS : ServiceCode.FAILED;
	}

	@Override
	public ServiceCode modifyModule(ProModuleVo proModuleVo) {

		proModuleVo.setUpdateTime(new Date());
		return 0 < proModuleMapper.updateByPrimaryKeySelective(proModuleVo) ? ServiceCode.SUCCESS : ServiceCode.FAILED;
	}

	@Override
	public BaseRes<List<ProProjectVo>> projectList() {
		
		BaseRes<List<ProProjectVo>> res = new BaseRes<List<ProProjectVo>>();
		List<ProProject> proProjectList = proProjectMapper.selectByExample(null);
		List<ProProjectVo> rows = new ArrayList<ProProjectVo>();
		for(ProProject project: proProjectList) {
			ProProjectVo mProProjectVo = new ProProjectVo();
			mProProjectVo.setId(project.getId());
			mProProjectVo.setName(project.getName());
			rows.add(mProProjectVo);
		}
		res.setServiceCode(ServiceCode.SUCCESS);
		res.setData(rows);
		return res;
		
	}

	@Override
	public BaseRes<List<ProModuleVo>> projectModuleList(Long projectId) {
		BaseRes<List<ProModuleVo>> res = new BaseRes<List<ProModuleVo>>();
		List<ProModule> proProjectList = proModuleMapper.selectByExample(null);
		List<ProModuleVo> rows = new ArrayList<ProModuleVo>();
		for(ProModule project: proProjectList) {
			ProModuleVo mProModuleVo = new ProModuleVo();
			mProModuleVo.setId(project.getId());
			mProModuleVo.setName(project.getName());
			mProModuleVo.setShowName(project.getShowName());
			rows.add(mProModuleVo);
		}
		res.setServiceCode(ServiceCode.SUCCESS);
		res.setData(rows);
		return res;
	}

}
